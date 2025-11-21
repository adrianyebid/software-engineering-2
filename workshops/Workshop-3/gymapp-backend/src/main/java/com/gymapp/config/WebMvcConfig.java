package com.gymapp.config;

import com.gymapp.interceptor.AuthenticationInterceptor;
import com.gymapp.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC configuration class to set up interceptors and message converters.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    @Autowired
    public WebMvcConfig(AuthenticationInterceptor authenticationInterceptor,
                        LoggingInterceptor loggingInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LoggingInterceptor debe ir primero para que el transactionId esté disponible desde el inicio
        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(authenticationInterceptor);
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new MappingJackson2HttpMessageConverter());
//    }
}
