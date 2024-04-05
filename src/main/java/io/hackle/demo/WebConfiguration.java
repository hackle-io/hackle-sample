package io.hackle.demo;

import io.hackle.sdk.HackleClient;
import io.hackle.sdk.HackleClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * application.properties 로 SDK key 관리
     */
    @Value("${hackle.sdk-key}")
    private String hackleSdkKey;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터 등록
        registry.addInterceptor(new HackleDeviceInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public HackleClient hackleClient() {
        // 서버용 sdk 초기화
        return HackleClients.create(hackleSdkKey);
    }
}
