package com.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class WebConfig {

    /**
     * 자바 객체를 JSON 객체로  변환시켜주는 용도
     * @return
     */
    @Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }
}
