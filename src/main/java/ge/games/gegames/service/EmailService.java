package ge.games.gegames.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    @Value("${spring.mail.from}")
    private String from;


    public void sendVerificationEmail(String to, String language, String verificationLink){

        Locale locale = Locale.forLanguageTag(language);

        String subject = messageSource.getMessage("email.subject.verify",null,locale);
        String text = messageSource.getMessage("email.text.verify",null, locale) + "/n" + verificationLink;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
