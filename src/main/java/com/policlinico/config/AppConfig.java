package com.policlinico.config;

import com.policlinico.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/cita/**", "/perfil", "/perfil/**", "/gestion")
                .excludePathPatterns("/login", "/registro", "/main", "/contacto",
                        "/publicidad", "/logout", "/especialidad/**");
    }
}
