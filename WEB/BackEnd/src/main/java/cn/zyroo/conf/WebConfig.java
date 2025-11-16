package cn.zyroo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("https://zyroo.cn", "https://www.zyroo.cn", "http://localhost:5173", "http://192.168.10.50:5173")
        .allowedMethods("*")
        .allowedHeaders("*");
  }
}