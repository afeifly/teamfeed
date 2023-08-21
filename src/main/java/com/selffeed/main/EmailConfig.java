package com.selffeed.main;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("email")
public class EmailConfig {
    @Getter
    @Setter
    private String smtp;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
}
