package kr.flap.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsInterceptor implements HandlerInterceptor {

  private final MeterRegistry meterRegistry;

  public MetricsInterceptor(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    request.setAttribute("startTime", System.nanoTime());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    Long startTime = (Long) request.getAttribute("startTime");
    if (startTime != null) {
      String uri = request.getRequestURI();
      Timer timer = Timer.builder("http.server.requests")
              .tag("uri", uri)
              .tag("method", request.getMethod())
              .tag("status", String.valueOf(response.getStatus()))
              .publishPercentileHistogram(true)
              .register(meterRegistry);
      timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
  }
}
