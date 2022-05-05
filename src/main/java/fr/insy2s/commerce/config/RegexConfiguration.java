package fr.insy2s.commerce.config;
import fr.insy2s.commerce.models.RegexCollection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class RegexConfiguration {

    @Bean
    public RegexCollection regexCollection(@Value("${environment.emailRegex}") String emailPattern,
                                           @Value("${environment.nameRegex}") String namePattern) {
        return new RegexCollection(Pattern.compile(emailPattern), Pattern.compile(namePattern));
    }
}