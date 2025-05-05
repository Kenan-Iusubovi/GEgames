package ge.games.gegames.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ResourceBundle;

@Configuration
public class LanguageConfig {

    @Bean
    public MessageSource messageSource(){

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages/messages");
        source.setDefaultEncoding("UTF-8");
        return source;

    }
}
