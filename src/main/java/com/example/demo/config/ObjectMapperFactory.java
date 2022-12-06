package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperFactory {

    ObjectMapper om() {
        return new ObjectMapper();
    }
}
