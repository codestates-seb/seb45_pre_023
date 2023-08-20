package sixman.stackoverflow.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.mockito.BDDMockito.given;

public class AuthIntegrationTest extends IntegrationTest{

    @TestFactory
    @DisplayName("회원가입 성공 테스트")
    Collection<DynamicTest> signup() {

        String email = "test@google.com";
        String password = "1q2w3e4r!";
        String nickname = "test";

        return List.of(
                DynamicTest.dynamicTest("이메일 인증 받기", () -> {
                    //given


                    //when


                    //then
                })
        );

    }

}
