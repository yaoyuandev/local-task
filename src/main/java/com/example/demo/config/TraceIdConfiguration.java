package com.example.demo.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceIdConfiguration {

    @Bean
    Filter traceIdInResponseFilter(Tracer tracer) {
        return (request, response, chain) -> {
            val currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                val resp = (HttpServletResponse) response;
                resp.addHeader("trace-id", currentSpan.context().traceId());
            }
            chain.doFilter(request, response);
        };
    }
}
