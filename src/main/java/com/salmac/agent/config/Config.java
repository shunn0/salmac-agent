package com.salmac.agent.config;

import com.salmac.agent.service.AgentService;
import com.salmac.agent.service.RestClient;
import com.salmac.agent.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Configuration
@EnableSwagger2
@EnableScheduling
public class Config {
    @Autowired
    RestClient restClient;

    @Scheduled(fixedRate = Constants.SERVER_STATUS_CHECK_INTERVAL)
    public void scheduleFixedRateTask() throws UnknownHostException {
        restClient.sentHeartBeatRequest();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowCredentials(true).allowedOrigins("*").allowedMethods("*");
//            }
//        };
//    }
}
