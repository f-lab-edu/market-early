package kr.flap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
  @Bean(name = "taskExecutor")
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);  // 기본 스레드 수
    executor.setMaxPoolSize(200);  // 최대 스레드 수
    executor.setQueueCapacity(500); // 작업 큐 크기
    executor.setThreadNamePrefix("Async-");
    executor.initialize();
    return executor;
  }
}
