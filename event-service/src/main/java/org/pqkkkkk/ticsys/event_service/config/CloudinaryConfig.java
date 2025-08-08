package org.pqkkkkk.ticsys.event_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CloudinaryConfig {
    private final Dotenv dotenv;

    public CloudinaryConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }
}
