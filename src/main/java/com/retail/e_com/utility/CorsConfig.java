package com.retail.e_com.utility;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer{
	
	@Override
    public void addCorsMappings(CorsRegistry registry) 
    {
        registry.addMapping("/api/") // Apply CORS configuration to all APIs, adjust the path as needed
            .allowedOrigins("http://localhost:5173") // Allow requests from this origin
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
            .allowCredentials(true)
            .allowedHeaders("*");
    }
}
