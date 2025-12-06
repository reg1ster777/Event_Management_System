package com.program.programdesignb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security 配置：关闭 CSRF、Basic/Form 登录，放行 /、静态资源、/api/**、/actuator/** 等，方便开发期无认证访问。
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());             // 演示期先关 CSRF
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html",
                        "/css/**", "/js/**", "/images/**",
                        "/api/**", "/actuator/**").permitAll() // 放行
                .anyRequest().permitAll()
        );
        http.httpBasic(basic -> basic.disable());      // 不要弹 Basic 登录框
        http.formLogin(form -> form.disable());        // 不要表单登录
        return http.build();
    }
}
