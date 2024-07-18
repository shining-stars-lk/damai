//package com.damai.config;
//
//import de.codecentric.boot.admin.server.config.AdminServerProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
///**
// * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
// * @description: 服务监控 鉴权
// * @author: 阿星不是程序员
// **/
//@Configuration
//public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
//
//    private final String adminContextPath;
//
//    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
//        this.adminContextPath = adminServerProperties.getContextPath();
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 登录成功处理类
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setTargetUrlParameter("redirectTo");
//        successHandler.setDefaultTargetUrl(adminContextPath + "/");
//
//        http.authorizeRequests()
//                //静态文件允许访问
//                .antMatchers(adminContextPath + "/assets/**").permitAll()
//                //登录页面允许访问
//                .antMatchers(adminContextPath + "/login", "/css/**", "/js/**", "/image/*").permitAll()
//                //其他所有请求需要登录
//                .anyRequest().authenticated()
//                .and()
//                //登录页面配置，用于替换security默认页面
//                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
//                //登出页面配置，用于替换security默认页面
//                .logout().logoutUrl(adminContextPath + "/logout").and()
//                .httpBasic().and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringAntMatchers(
//                        "/instances",
//                        "/actuator/**"
//                );
//
//    }
//
//}