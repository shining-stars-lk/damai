package com.damai.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 服务监控权限 配置
 * @author: 阿星不是程序员
 **/
@Configuration
public class SecurityConfig {
 
    private final String adminContextPath;
 
    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }
 
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(adminContextPath + "/assets/**").permitAll()
                    .requestMatchers(adminContextPath + "/login", "/css/**", "/js/**", "/image/*").permitAll()
                    .requestMatchers(adminContextPath + "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage(adminContextPath + "/login")
                    .successHandler(successHandler))
            .logout(logout -> logout
                    .logoutUrl(adminContextPath + "/logout"))
            .httpBasic(withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/instances", "/actuator/**","/v3/api-docs/**",
                    adminContextPath + "/instances/**", adminContextPath + "/actuator/**", adminContextPath + "/v3/api-docs/**"));    
        return http.build();
    }
}