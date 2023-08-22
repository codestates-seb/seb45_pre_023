package sixman.stackoverflow.domain.reply.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyUpdateApiRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReplyControllerTest extends ControllerTest {

    @Test
    @DisplayName("댓글 생성 API")
    void createReply() throws Exception {
        //given
        Long answerId = 1L;
        Long createdReplyId = 1L;
        ReplyCreateApiRequest request = ReplyCreateApiRequest.builder()
                .content("reply content")
                .build();

        String content = objectMapper.writeValueAsString(request);

        given(replyService.createReply(any(), any())).willReturn(createdReplyId);

        //when
        ResultActions actions = mockMvc.perform(
                post("/answers/{answer-id}/replies", answerId)
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/replies/" + createdReplyId));


        //restDocs
        setConstraintClass(ReplyCreateApiRequest.class);

        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("답변 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용").attributes(getConstraint("content"))
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성된 댓글 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("댓글 목록 조회 API")
    void getReplies() throws Exception {
        //given
        Long answerId = 1L;
        Long page = 1L;

        List<ReplyResponse> replyResponses = getReplyResponses(1L);
        PageImpl<ReplyResponse> pageResponse = new PageImpl<>(replyResponses, PageRequest.of(page.intValue(), 5), 20);

        given(replyService.getRepliesPaged(anyLong(), any(Pageable.class))).willReturn(pageResponse);

        String result = objectMapper.writeValueAsString(ApiPageResponse.ok(pageResponse));

        //when
        ResultActions actions = mockMvc.perform(
                get("/answers/{answer-id}/replies", answerId)
                        .param("page", String.valueOf(page))
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("답변 ID")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("data").description("댓글 목록"),
                                fieldWithPath("data[].replyId").description("댓글 ID"),
                                fieldWithPath("data[].content").description("댓글 내용"),
                                fieldWithPath("data[].member.memberId").description("댓글 작성자 ID"),
                                fieldWithPath("data[].member.nickname").description("댓글 작성자 닉네임"),
                                fieldWithPath("data[].member.imageUrl").description("댓글 작성자 이미지 URL"),
                                fieldWithPath("data[].createdDate").description("댓글 생성일"),
                                fieldWithPath("data[].updatedDate").description("댓글 수정일"),
                                fieldWithPath("pageInfo.page").description("댓글 현재 페이지"),
                                fieldWithPath("pageInfo.size").description("댓글 페이지 사이즈"),
                                fieldWithPath("pageInfo.totalPage").description("댓글 전체 페이지 수"),
                                fieldWithPath("pageInfo.totalSize").description("댓글 전체 개수"),
                                fieldWithPath("pageInfo.first").description("댓글 첫 페이지 여부"),
                                fieldWithPath("pageInfo.last").description("댓글 마지막 페이지 여부"),
                                fieldWithPath("pageInfo.hasNext").description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.hasPrevious").description("이전 페이지가 있는지"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                )
        );
    }

    @Test
    @DisplayName("댓글 단건 조회 API")
    void getReply() throws Exception {
        //given
        Long replyId = 1L;

        ReplyResponse replyResponse = ReplyResponse.builder()
                .replyId(replyId)
                .content("reply content")
                .member(MemberInfo.of(createMember(1L)))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        given(replyService.findReply(anyLong())).willReturn(replyResponse);

        String result = objectMapper.writeValueAsString(ApiSingleResponse.ok(replyResponse));

        //when
        ResultActions actions = mockMvc.perform(
                get("/replies/{reply-id}", replyId)
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("reply-id").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("data.replyId").description("댓글 ID"),
                                fieldWithPath("data.content").description("댓글 내용"),
                                fieldWithPath("data.member.memberId").description("댓글 작성자 ID"),
                                fieldWithPath("data.member.nickname").description("댓글 작성자 닉네임"),
                                fieldWithPath("data.member.imageUrl").description("댓글 작성자 이미지 URL"),
                                fieldWithPath("data.createdDate").description("댓글 생성일"),
                                fieldWithPath("data.updatedDate").description("댓글 수정일"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                )
        );


    }

    @Test
    @DisplayName("댓글 수정 API")
    void updateReply() throws Exception {
        //given
        Long replyId = 1L;
        setDefaultAuthentication(1L);

        ReplyUpdateApiRequest request = ReplyUpdateApiRequest.builder()
                .content("update content")
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/replies/{reply-id}", replyId)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        setConstraintClass(ReplyUpdateApiRequest.class);

        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("reply-id").description("댓글 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정할 댓글 내용").attributes(getConstraint("content"))
                        )
                )
        );
    }

    @Test
    @DisplayName("댓글 삭제 API")
    void deleteReply() throws Exception {
        //given
        Long replyId = 1L;
        setDefaultAuthentication(1L);

        //when
        ResultActions actions = mockMvc.perform(
                delete("/replies/{reply-id}", replyId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("reply-id").description("삭제할 댓글 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        )
                )
        );
    }

    @Test
    @DisplayName("댓글 생성 시 validation 검증 - content 가 null 일 때")
    void createReplyValidation() throws Exception {
        //given
        Long answerId = 1L;
        Long createdReplyId = 1L;
        ReplyCreateApiRequest request = ReplyCreateApiRequest.builder()
                .build();

        String content = objectMapper.writeValueAsString(request);

        given(replyService.createReply(any(), any())).willReturn(createdReplyId);

        //when
        ResultActions actions = mockMvc.perform(
                post("/answers/{answer-id}/replies", answerId)
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].field").value("content"))
                .andExpect(jsonPath("$.data[0].value").value("null"))
                .andExpect(jsonPath("$.data[0].reason").value("댓글 내용을 입력해주세요."));
    }

    @Test
    @DisplayName("댓글 수정 시 validation 검증 - content 가 null 일 때")
    void updateReplyValidation() throws Exception {
        //given
        Long replyId = 1L;
        setDefaultAuthentication(1L);

        ReplyUpdateApiRequest request = ReplyUpdateApiRequest.builder()
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/replies/{reply-id}", replyId)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].field").value("content"))
                .andExpect(jsonPath("$.data[0].value").value("null"))
                .andExpect(jsonPath("$.data[0].reason").value("댓글 내용을 입력해주세요."));
    }
}