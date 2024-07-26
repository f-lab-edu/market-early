package kr.flap.config;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ThreadPoolAdjuster {

  private ThreadPoolTaskExecutor taskExecutor;

  @Scheduled(fixedRate = 1000) // 5초마다 실행
  public void adjustThreadPool() {
    int activeCount = taskExecutor.getActiveCount();
    int poolSize = taskExecutor.getPoolSize();
    int maxPoolSize = taskExecutor.getMaxPoolSize();

    // 스레드 풀의 상태를 로그로 출력
    System.out.println("Active Threads: " + activeCount + ", Pool Size: " + poolSize + ", Max Pool Size: " + maxPoolSize);

    // 사용 중인 스레드가 80% 이상이면 스레드 풀을 확장
    if (activeCount > poolSize * 0.8 && poolSize < maxPoolSize) {
      taskExecutor.setCorePoolSize(Math.min(poolSize + 10, maxPoolSize));
      System.out.println("Increased core pool size to: " + taskExecutor.getCorePoolSize());
    }

    // 사용 중인 스레드가 20% 미만이면 스레드 풀을 축소
    if (activeCount < poolSize * 0.2 && poolSize > 50) { // 최소 50개의 스레드를 유지
      taskExecutor.setCorePoolSize(Math.max(poolSize - 10, 50));
      System.out.println("Decreased core pool size to: " + taskExecutor.getCorePoolSize());
    }
  }
}
