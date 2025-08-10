package org.pqkkkkk.ticsys.event_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotEnvConfig {
    @Bean
    public Dotenv dotenv(){
        return Dotenv.load();
    }
}
