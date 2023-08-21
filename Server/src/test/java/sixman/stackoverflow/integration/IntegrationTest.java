package sixman.stackoverflow.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questionrecommend.repository.QuestionRecommendRepository;
import sixman.stackoverflow.domain.questiontag.QuestionTagRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.tag.repository.TagRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.module.email.service.MailService;
import sixman.stackoverflow.module.redis.service.RedisService;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTest {

    @Autowired private TokenProvider tokenProvider;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected QuestionRepository questionRepository;
    @Autowired protected AnswerRepository answerRepository;
    @Autowired protected ReplyRepository replyRepository;
    @Autowired protected TagRepository tagRepository;
    @Autowired protected QuestionTagRepository questionTagRepository;
    @Autowired protected QuestionRecommendRepository questionRecommendRepository;
    @Autowired protected AnswerRecommendRepository answerRecommendRepository;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected EntityManager entityManager;

    @MockBean protected MailService mailService;
    @MockBean protected RedisService redisService;
    @MockBean protected RestTemplate restTemplate;
    @MockBean protected DefaultOAuth2UserService defaultOAuth2UserService;

    protected void deleteAll(){
        answerRecommendRepository.deleteAll();
        questionRecommendRepository.deleteAll();
        questionTagRepository.deleteAll();
        tagRepository.deleteAll();
        replyRepository.deleteAll();
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    protected void flushAll(){
        answerRecommendRepository.flush();
        questionRecommendRepository.flush();
        questionTagRepository.flush();
        tagRepository.flush();
        replyRepository.flush();
        answerRepository.flush();
        questionRepository.flush();
        memberRepository.flush();
        entityManager.clear();
    }

    private String createAccessToken(Member member, long accessTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateAccessToken(authenticationToken, accessTokenExpireTime);
    }

    private String createRefreshToken(Member member, long refreshTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateRefreshToken(authenticationToken, refreshTokenExpireTime);
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(
                member.getMemberId(),
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    protected Member createAndSaveMember(String email){

        Member member = createMember(email);

        return memberRepository.save(member);
    }

    protected Member createAndSaveMember(String email, String password){

        Member member = createMember(email);
        member.updatePassword(passwordEncoder.encode(password));

        return memberRepository.save(member);
    }

    protected Member createAndSaveMemberDisable(String email){

        Member member = createMember(email);
        member.disable();

        return memberRepository.save(member);
    }

    private Member createMember(String email) {
        Member member = Member.builder()
                .email(email)
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();
        return member;
    }

    protected Question createAndSaveQuestion(Member member) {
        Question question = Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .build();

        questionRepository.save(question);

        return question;
    }

    protected Answer createAndSaveAnswer(Member member, Question question) {
        Answer answer = Answer.builder()
                .member(member)
                .content("content")
                .question(question)
                .build();

        answerRepository.save(answer);

        return answer;
    }

    protected Reply createAndSaveReply(Member member, Answer answer) {
        Reply reply = Reply.builder()
                .member(member)
                .content("content")
                .answer(answer)
                .build();

        replyRepository.save(reply);

        return reply;
    }

    protected Tag createAndSaveTag(String tagName) {
        Tag tag = Tag.builder()
                .tagName(tagName)
                .build();

        tagRepository.save(tag);

        return tag;
    }

    protected void useTagInQuestion(Question question, Tag tag) {
        QuestionTag questionTag = QuestionTag.builder()
                .question(question)
                .tag(tag)
                .build();

        questionTagRepository.save(questionTag);
    }

    //추천 기능이 완성되면 주석 풀 예정
    /*protected void recommendQuestion(Member member, Question question, TypeEnum typeEnum) {

        QuestionRecommend questionRecommend = QuestionRecommend.builder()
                .member(member)
                .question(question)
                .type(typeEnum)
                .build();

        question.applyRecommend(TypeEnum.UPVOTE);

        questionRepository.save(question);
        questionRecommendRepository.save(questionRecommend);
    }

    protected void recommendAnswer(Member member, Answer answer, TypeEnum typeEnum) {

        AnswerRecommend answerRecommend = AnswerRecommend.builder()
                .member(member)
                .answer(answer)
                .type(typeEnum)
                .build();

        if(typeEnum == TypeEnum.UPVOTE)
            answer.setRecommend(answer.getRecommend() + 1);
        else
            answer.setRecommend(answer.getRecommend() - 1);

        answerRepository.save(answer);
        answerRecommendRepository.save(answerRecommend);
    }*/

    protected <T> ApiSingleResponse<T> getApiSingleResponseFromResult(ResultActions actions, Class<T> clazz) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = actions.andReturn().getResponse().getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiSingleResponse.class, clazz);

        return objectMapper.readValue(contentAsString, javaType);
    }

    protected <T> ApiPageResponse<T> getApiPageResponseFromResult(ResultActions actions, Class<T> clazz) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = actions.andReturn().getResponse().getContentAsString();

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiPageResponse.class, clazz);

        return objectMapper.readValue(contentAsString, javaType);
    }


}
