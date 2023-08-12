package sixman.stackoverflow.module.email.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailSendException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Transactional
@Service
@Slf4j
public class MailService {

    private final JavaMailSender emailSender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String sendAuthEmail(String toEmail) {
        String title = "Sixman-Stackoverflow 회원가입 인증 번호";
        String code = createCode();

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();

            createEmailForm(toEmail, title, code, mimeMessage);

            emailSender.send(mimeMessage);

            return code;
        } catch (Exception e) {
            throw new EmailSendException();
        }
    }

    private void createEmailForm(String toEmail, String title, String code, MimeMessage mimeMessage) throws MessagingException {

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = "<h3>회원가입 인증 번호</h3>"
                +"<p>아래의 인증 번호를 입력해주세요.</p>"
                + "<div style='border: 1px solid #e1e1e1; padding: 10px; width: 100px; text-align: center;'>"
                + code
                + "</div>";

        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(htmlContent, true);
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                builder.append(characters.charAt(randomIndex));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.info("create mail authCode error : " + e.getMessage());
            throw new EmailSendException();
        }
    }

}
