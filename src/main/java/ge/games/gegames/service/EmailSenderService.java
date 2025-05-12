package ge.games.gegames.service;

import ge.games.gegames.enums.LanguageE;
import ge.games.gegames.exception.EmailSendException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Value("${spring.mail.from}")
    private String from;


    public void sendVerificationEmail(String to, LanguageE language, String code) {
        try {
            String htmlBody = loadVerificationEmail(code, language);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            if (LanguageE.EN.equals(language)) {
                helper.setSubject("GEgames.ge Verification code");
            }
            if (LanguageE.KA.equals(language)){
                helper.setSubject("GEgames.ge Ვერიფიკაციის კოდი");
            }
            helper.setText(htmlBody, true);
            helper.addInline("gameboard",
                    new ClassPathResource("static/images/email_background.png"));

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("Failed to send email", e);
        }
    }


    public String loadVerificationEmail(String code, LanguageE language) throws IOException {

        Resource resource = getVerificationEmailTemplate(language);

        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        return content.replace("{{code}}", code);
    }

    public Resource getVerificationEmailTemplate(LanguageE language) {
        return switch (language) {
            case KA -> new ClassPathResource("templates/verify_email_ka.html");
            case EN -> new ClassPathResource("templates/verify_email_en.html");
            default -> new ClassPathResource("templates/verify_email_en.html");
        };
    }

}
