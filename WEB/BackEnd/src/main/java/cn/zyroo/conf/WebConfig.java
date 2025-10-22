package cn.zyroo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("https://zyroo.cn", "http://localhost:5173", "http://localhost:8085")
        .allowedMethods("*")
        .allowedHeaders("*");
  }
}