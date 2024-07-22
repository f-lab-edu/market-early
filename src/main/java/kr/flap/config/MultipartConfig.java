package kr.flap.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {
  @Value("${file.multipart.maxUploadSize}")
  private long maxUploadSize;

  @Value("${file.multipart.maxUploadSizePerFile}")
  private long maxUploadSizePerFile;

  @Bean
  public MultipartResolver multipartResolver() {
    StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
    return multipartResolver;
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxRequestSize(DataSize.ofBytes(maxUploadSize));
    factory.setMaxFileSize(DataSize.ofBytes(maxUploadSizePerFile));

    return factory.createMultipartConfig();
  }
}
