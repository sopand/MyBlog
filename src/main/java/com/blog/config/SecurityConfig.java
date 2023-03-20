package com.blog.config;


import com.blog.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    public static final String[] SECURITY_EXCLUDE_PATTERN_ARR = {
            "/css/**", "/font/**", "/cmsimg/**", "/favicon.ico",
            "/error", "/css/js/**"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // css,js,img등의 시큐리티 필터적용이 필요없는 자원에 대한 접근을 설정,
        return (web) -> web.ignoring().requestMatchers(SECURITY_EXCLUDE_PATTERN_ARR);
    }

    @Bean
    public SecurityFilterChain StudentFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests()
                .requestMatchers("/**")
                .permitAll();
        http.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
    }

}
