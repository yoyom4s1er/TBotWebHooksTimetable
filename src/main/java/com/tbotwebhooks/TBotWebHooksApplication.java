package com.tbotwebhooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.KeyStore;
import java.security.KeyStoreException;

@SpringBootApplication
@EnableScheduling
public class TBotWebHooksApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(TBotWebHooksApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TBotWebHooksApplication.class);
    }
}
