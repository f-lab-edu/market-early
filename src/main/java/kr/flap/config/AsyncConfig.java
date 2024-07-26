package kr.flap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50); // 기본 스레드 수를 늘림
    executor.setMaxPoolSize(100); // 최대 스레드 수를 늘림
    executor.setQueueCapacity(200); // 작업 큐 크기를 늘림
    executor.setThreadNamePrefix("Async-");
    executor.initialize();
    return executor;
  }
}
