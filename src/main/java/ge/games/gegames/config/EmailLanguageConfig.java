package ge.games.gegames.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class EmailLanguageConfig {

    @Bean
    public MessageSource messageSource(){

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages/messages");
        source.setDefaultEncoding("UTF-8");
        return source;

    }
}
