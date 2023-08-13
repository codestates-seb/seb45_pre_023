package sixman.stackoverflow.global.restdocs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.auth.oauth.service.Provider;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.global.common.CommonController;
import sixman.stackoverflow.global.entity.BaseEnum;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sixman.stackoverflow.global.restdocs.utils.CustomResponseFieldsSnippet.customResponseFields;

public class CommonControllerTest extends ControllerTest {

    @Test
    @DisplayName("기본 응답값 API")
    void basicAPI() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/common/success/single")
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(documentHandler.document(
                        responseFields(
                                fieldWithPath("data").type(VARIES).description("응답 데이터 (본문)"),
                                fieldWithPath("code").type(NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                fieldWithPath("message").type(STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("기본 페이징 응답값 API ")
    void basicPagingAPI() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/common/success/page")
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(documentHandler.document(
                        responseFields(
                                fieldWithPath("data").type(VARIES).description("응답 데이터 (본문)"),
                                fieldWithPath("pageInfo").description("페이징 정보"),
                                fieldWithPath("pageInfo.page").description("현재 페이지"),
                                fieldWithPath("pageInfo.size").description("페이지 사이즈"),
                                fieldWithPath("pageInfo.totalPage").description("전체 페이지 수"),
                                fieldWithPath("pageInfo.totalSize").description("전체 개수"),
                                fieldWithPath("pageInfo.first").description("첫 페이지 여부"),
                                fieldWithPath("pageInfo.last").description("마지막 페이지 여부"),
                                fieldWithPath("pageInfo.hasNext").description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.hasPrevious").description("이전 페이지가 있는지"),
                                fieldWithPath("code").type(NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                fieldWithPath("message").type(STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("Business 예외 API")
    void exceptionAPI() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(get("/common/errors"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(documentHandler.document(
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(STRING).description("예외 상태"),
                                fieldWithPath("message").type(STRING).description("예외 메세지"),
                                fieldWithPath("data").type(NULL).description("null 값")
                        )
                ));

    }

    @Test
    @DisplayName("Validation 예외 API")
    void exceptionValidAPI() throws Exception {
        //given
        CommonController.SampleRequest request = new CommonController.SampleRequest("", "");

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(
                post("/common/errors/validation")
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(documentHandler.document(
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(STRING).description("응답 상태"),
                                fieldWithPath("message").type(STRING).description("응답 메세지"),
                                fieldWithPath("data").type(ARRAY).description("예외 리스트"),
                                fieldWithPath("data[].field").type(STRING).description("예외 발생 필드"),
                                fieldWithPath("data[].value").type(STRING).description("예외 발생 값"),
                                fieldWithPath("data[].reason").type(STRING).description("예외 발생 이유")
                        )
                ));
    }

    @Test
    @DisplayName("restdocs 용 enum 조회 API")
    void enums() throws Exception {
        //given
        List<String> enumValues = creatEnumRequest(
                Authority.class,
                TypeEnum.class,
                Provider.class
        );

        String content = objectMapper.writeValueAsString(enumValues);

        //when
        ResultActions actions = mockMvc.perform(
                post("/common/enums")
                        .contentType(APPLICATION_JSON)
                        .content(content));

        //then
        Map<String, Map<String, String>> enums = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), HashMap.class);

        // enum 문서화를 위한 Snippets 생성
        Snippet[] snippets = new Snippet[enums.size()];
        for(int i = 0; i < enums.size(); i++) {

            String enumName = enums.keySet().toArray()[i].toString();

            snippets[i] = customResponseFields("custom-response",
                    beneathPath(enumName).withSubsectionId(enumName),
                    attributes(getTitle(enumName)),
                    enumConvertFieldDescriptor(enums.get(enumName))
            );
        }

        actions
                .andExpect(status().isOk())
                .andDo(
                        documentHandler.document(
                                snippets
                        )
                );

    }

    // Map 으로 넘어온 enumValue 를 fieldWithPath 로 변경하여 리턴
    private FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    @SafeVarargs
    private List<String> creatEnumRequest(Class<? extends BaseEnum>... enums) {

        List<String> enumValues = new ArrayList<>();
        for (Class<?> e : enums) {

            enumValues.add(e.getName());
        }
        return enumValues;
    }

    private Attributes.Attribute getTitle(
            final String value){
        return new Attributes.Attribute("title",value);
    }
}
