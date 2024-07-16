//package com.damai.conf.swagger;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//import springfox.documentation.swagger.web.SecurityConfiguration;
//import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//import springfox.documentation.swagger.web.UiConfiguration;
//import springfox.documentation.swagger.web.UiConfigurationBuilder;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
// * @description: swagger 处理器
// * @author: 阿星不是程序员
// **/
//@RestController
//@RequestMapping("/swagger-resources")
//public class SwaggerHandler {
//    
//    private final SecurityConfiguration securityConfiguration;
//    private final UiConfiguration uiConfiguration;
//    private final SwaggerResourcesProvider swaggerResources;
//    
//    @Autowired
//    public SwaggerHandler(SwaggerResourcesProvider swaggerResources, SecurityConfiguration securityConfiguration, UiConfiguration uiConfiguration) {
//        this.swaggerResources = swaggerResources;
//        this.securityConfiguration = securityConfiguration;
//        this.uiConfiguration = uiConfiguration;
//    }
//    
//    @GetMapping("/configuration/security")
//    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
//        return Mono.just(new ResponseEntity<>(
//                Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()),
//                HttpStatus.OK));
//    }
//    
//    @GetMapping("/configuration/ui")
//    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
//        return Mono.just(new ResponseEntity<>(
//                Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
//    }
//    
//    @GetMapping
//    public Mono<ResponseEntity<List<SwaggerResource>>> swaggerResources() {
//        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
//    }
//    
//}
