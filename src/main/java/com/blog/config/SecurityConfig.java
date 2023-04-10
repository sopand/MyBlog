package com.blog.config;


import com.blog.oAuth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public BCryptPasswordEncoder passwordEncoder() {
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
        http.formLogin().disable()
                .oauth2Login().loginPage("/users")
                .defaultSuccessUrl("/index", true)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http.logout()
                .logoutUrl("/users/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/index") // 로그아웃 성공 후 이동페이지
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll();
        return http.build();
    }

}
