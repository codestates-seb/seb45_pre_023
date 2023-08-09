package sixman.stackoverflow.domain.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

class MemberControllerTest extends ControllerTest {

    @Test
    void signup() throws Exception {
        //given
        MemberCreateApiRequest request = MemberCreateApiRequest.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234abcd!")
                .build();

        Long memberId = 1L;

        given(memberService.signup(any(MemberCreateServiceRequest.class))).willReturn(memberId);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + memberId));

        //restdocs
        actions.andDo(documentHandler.document(
                requestFields(
                        fieldWithPath("email").description("회원 email"),
                        fieldWithPath("nickname").description("회원 nickname"),
                        fieldWithPath("password").description("회원 password")
                ),
                responseHeaders(
                        headerWithName("Location").description("생성된 회원의 URI")
                )
        ));


    }

}