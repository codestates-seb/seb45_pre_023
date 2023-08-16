package sixman.stackoverflow.global.testhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import sixman.stackoverflow.auth.controller.AuthController;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.oauth.service.OAuthService;
import sixman.stackoverflow.domain.answer.controller.AnswerController;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.answerrecommend.service.AnswerRecommendService;
import sixman.stackoverflow.domain.member.controller.MemberController;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.MemberService;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.controller.QuestionController;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.reply.controller.ReplyController;
import sixman.stackoverflow.domain.reply.service.ReplyService;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.common.CommonController;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.PageInfo;
import sixman.stackoverflow.module.aws.service.S3Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MemberController.class, AuthController.class, CommonController.class, QuestionController.class, AnswerController.class, ReplyController.class})
@ExtendWith({RestDocumentationExtension.class})
@ActiveProfiles("local")
public abstract class ControllerTest {

    @MockBean protected MemberService memberService;
    @MockBean protected AnswerService answerService;
    @MockBean protected OAuthService oAuthService;
    @MockBean protected MemberRepository memberRepository;
    @MockBean protected QuestionService questionService;
    @MockBean protected S3Service s3Service;
    @MockBean protected ReplyService replyService;
    @MockBean protected AnswerRecommendService answerRecommendService;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    protected RestDocumentationResultHandler documentHandler;

    @Autowired private MessageSource messageSource;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();
    private BeanDescriptor beanDescriptor;

    @BeforeEach
    void setUp(WebApplicationContext context,
               final RestDocumentationContextProvider restDocumentation,
               TestInfo testInfo) {


        String className = testInfo.getTestClass().orElseThrow().getSimpleName()
                .replace("ControllerTest", "").toLowerCase();
        String methodName = testInfo.getTestMethod().orElseThrow().getName().toLowerCase();

        documentHandler = document(
                className + "/" + methodName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );

        DefaultMockMvcBuilder mockMvcBuilder = webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true));


        //validation 은 문서화하지 않음
        if(!methodName.contains("validation")){
            mockMvcBuilder.alwaysDo(documentHandler);
        }

        mockMvc = mockMvcBuilder.build();
    }

    protected static String generateLinkCode(Class<?> clazz) {
        return String.format("link:/common/%s.html[%s 값 보기,role=\"popup\"]",
                clazz.getSimpleName().toLowerCase(), clazz.getSimpleName());
    }

    protected Attributes.Attribute getConstraint(String value){

        assert(beanDescriptor != null) : "constraint 설정이 되어있지 않습니다. setConstraintClass() 를 통해 설정해주세요 ";

        PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty(value);

        StringBuilder sb = new StringBuilder();

        if(propertyDescriptor == null){
            return new Attributes.Attribute("constraints", sb.toString());
        }

        Set<ConstraintDescriptor<?>> constraintDescriptors = propertyDescriptor.getConstraintDescriptors();

        for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {

            String type = constraintDescriptor.getAnnotation().annotationType().getSimpleName();

            String message = (String) constraintDescriptor.getAttributes().get("message");
            Integer min = (Integer) constraintDescriptor.getAttributes().get("min");
            Integer max = (Integer) constraintDescriptor.getAttributes().get("max");
            String actualMessage = getActualMessage(message, min, max);

            sb.append(" [");
            sb.append(type);
            sb.append(" : ");
            sb.append(actualMessage);
            sb.append("] ");
        }

        return new Attributes.Attribute("constraints", sb.toString());
    }

    protected void setConstraintClass(Class<?> clazz){
        this.beanDescriptor = validator.getConstraintsForClass(clazz);
    }

    protected String getActualMessage(String messageKey, Integer min, Integer max) {
        String actualMessageKey = messageKey.replace("{", "").replace("}", "");

        String message = messageSource.getMessage(actualMessageKey, null, Locale.getDefault());

        if(min == null || max == null){
            return message;
        }

        return message.replace("{min}", min.toString()).replace("{max}", max.toString());
    }

    protected Member createMember() {
        return Member.builder()
                .email("test@google.com")
                .nickname("test")
                .password("1234abcd!")
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("test url").build())
                .enabled(true)
                .build();
    }

    protected Member createMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .email("test@google.com")
                .nickname("test")
                .password("1234abcd!")
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("test url").build())
                .enabled(true)
                .build();
    }

    protected void setDefaultAuthentication(Long id){
        UserDetails userDetails = createUserDetails(id, createMember());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextImpl securityContext = new SecurityContextImpl(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    private UserDetails createUserDetails(Long id, Member notSavedmember) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(notSavedmember.getAuthority().toString());

        return new CustomUserDetails(
                id,
                String.valueOf(notSavedmember.getEmail()),
                notSavedmember.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    protected QuestionDetailResponse.QuestionAnswer createAnswerResponse() {

        List<AnswerResponse> answers = createAnswers();

        Page<AnswerResponse> page = new PageImpl<>(answers, PageRequest.of(0, 5), 20);
        PageInfo pageInfo = PageInfo.of(page);

        return QuestionDetailResponse.QuestionAnswer.builder()
                .answers(answers)
                .pageInfo(pageInfo)
                .build();
    }

    protected List<AnswerResponse> createAnswers() {
        List<AnswerResponse> answers = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            TypeEnum type;
            if(i % 2 == 0) {
                type = TypeEnum.UPVOTE;
            } else {
                type = TypeEnum.DOWNVOTE;
            }

            AnswerResponse answer = AnswerResponse.builder()
                    .answerId((long) i)
                    .content("content")
                    .member(MemberInfo.of(createMember((long) i)))
                    .recommend(10)
                    .recommendType(type)
                    .reply(createReplyResponse(i))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            answers.add(answer);
        }
        return answers;
    }

    protected AnswerResponse.AnswerReply createReplyResponse(int index) {

        List<ReplyResponse> replies = getReplyResponses((long) index);

        Page<ReplyResponse> page = new PageImpl<>(replies, PageRequest.of(0, 5), 20);
        PageInfo pageInfo = PageInfo.of(page);

        return AnswerResponse.AnswerReply.builder()
                .replies(replies)
                .pageInfo(pageInfo)
                .build();
    }

    protected List<ReplyResponse> getReplyResponses(long index) {
        List<ReplyResponse> replies = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            ReplyResponse reply = ReplyResponse.builder()
                    .replyId(index * 5 + i)
                    .content("content")
                    .member(MemberInfo.of(createMember((long) i)))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            replies.add(reply);
        }
        return replies;
    }

}
