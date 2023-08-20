package sixman.stackoverflow.module.email.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import sixman.stackoverflow.module.ModuleServiceTest;

import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MailServiceTest extends ModuleServiceTest {

    @Autowired MailService mailService;
    @Autowired JavaMailSender emailSender;
    @Test
    @DisplayName("이메일 인증 메일을 보내고 코드를 반환한다.")
    void sendAuthEmail() {
        //given
        String email = "test@google.com";

        //when
        String code = mailService.sendAuthEmail(email);

        //then
        assertThat(code).hasSize(6);
    }
}