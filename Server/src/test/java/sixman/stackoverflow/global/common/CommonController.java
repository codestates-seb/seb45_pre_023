package sixman.stackoverflow.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.global.entity.BaseEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/success/single")
    public ResponseEntity<ApiSingleResponse<String>> mockResponseSuccess() {

        ApiSingleResponse<String> response = ApiSingleResponse.ok("success data");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success/page")
    public ResponseEntity<ApiPageResponse<String>> mockResponseSuccessPage() {

        List<String> content = List.of("data1", "data2", "data3", "data4", "data5");
        PageImpl<String> page = new PageImpl<>(content, PageRequest.of(0, 5), 15);
        ApiPageResponse<String> response = ApiPageResponse.ok(page);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/errors")
    public ResponseEntity<Void> mockResponseError() {

        throw new MemberNotFoundException();
    }

    @PostMapping("/errors/validation")
    public ResponseEntity<Void> mockResponseErrorValidation(@RequestBody @Valid SampleRequest dto) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/enums")
    public ResponseEntity<Map<String, Map<String, String>>> findEnums(@RequestBody List<String> paths) throws Exception {

        //ocp 를 위한 온몸 비틀기
        Map<String,Map<String, String>> enums = new HashMap<>();

        for(String path : paths) {
            Class clazz = Class.forName(path);
            enums.put(clazz.getSimpleName(), getDocs((BaseEnum[]) clazz.getMethod("values").invoke(null)));
        }

        return ResponseEntity.ok(enums);
    }

    private Map<String, String> getDocs(BaseEnum[] baseEnums) {
        return Arrays.stream(baseEnums)
                .collect(Collectors.toMap(BaseEnum::getName, BaseEnum::getDescription));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleRequest {


        @Email(message="{validation.member.email}")
        @NotBlank(message = "{validation.member.email}")
        private String email;

        @NotBlank(message="{validation.member.password}")
        private String password;
    }
}
