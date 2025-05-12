package ge.games.gegames.service;

import ge.games.gegames.exception.EmailTemplateException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Value("${spring.mail.from}")
    private String from;


    public void sendVerificationEmail(String to, String code) {
        try {
            String htmlBody = loadVerificationEmail(code);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Ვერიფიკაციის კოდი");
            helper.setText(htmlBody, true); // 'true' enables HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("Failed to send email", e);
        }
    }


    public String loadVerificationEmail(String code) throws IOException {
        Resource resource = new ClassPathResource("templates/verify_email_ka.html");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        return content.replace("{{code}}", code);
    }
}
