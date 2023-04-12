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
    /**
     * 권한 처리전에 허용할 CSS/JS/폰트등에 대한 설정을 해줄 이름들을 작성하는 용도
     */
    public static final String[] SECURITY_EXCLUDE_PATTERN_ARR = {
            "/css/**", "/font/**", "/myblog/**", "/favicon.ico",
            "/error", "/js/**","/img/**"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security가 작동하기 전에 먼저 체크하여 해당하는 주소들에 대한 접근제한이 해제됨.
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // css,js,img등의 시큐리티 필터적용이 필요없는 자원에 대한 접근을 설정,
        return (web) -> web.ignoring().requestMatchers(SECURITY_EXCLUDE_PATTERN_ARR);
    }

    @Bean
    public SecurityFilterChain StudentFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/boards","/boards/menu/**","/boards/detail/**","/index","/users","/boards/review/list","/boards/review/parent")
                .permitAll()
                .requestMatchers("/boards/review","/users/**").hasAnyRole("USER") //유저라면 리뷰작성과 로그인/아웃에 제약이 없도록 설정
                .requestMatchers("/**").hasRole("ADMIN").anyRequest().authenticated(); // 어드민은 모든 기능에대한 권한이 있도록
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
