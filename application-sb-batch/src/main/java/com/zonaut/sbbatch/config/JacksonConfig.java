package com.zonaut.sbbatch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zonaut.common.Common.OBJECT_MAPPER;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

}
