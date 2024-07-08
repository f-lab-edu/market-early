package kr.flap.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class SeederPerformanceAspect {
  private static final Logger logger = LoggerFactory.getLogger(SeederPerformanceAspect.class);

  // BaseSeeder의 seed 메서드 실행을 포인트컷으로 정의
  @Pointcut("execution(* kr.flap.domain.data.BaseSeeder.seed(..))")
  public void seedMethodExecution() {}

  // connect 메서드들의 실행을 포인트컷으로 정의
  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectUserToUserAddress(..))")
  public void connectUserToUserAddressExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectUserToCart(..))")
  public void connectUserToCartExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectUserToReserve(..))")
  public void connectUserToReserveExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectUserToOrder(..))")
  public void connectUserToOrderExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectOrderToProduct(..))")
  public void connectOrderToProductExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectOrderToDelivery(..))")
  public void connectOrderToDeliveryExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectProductToCart(..))")
  public void connectProductToCartExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectProductToSeller(..))")
  public void connectProductToSellerExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectProductToStorage(..))")
  public void connectProductToStorageExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectToSubProductToProduct(..))")
  public void connectToSubProductToProductExecution() {}

  @Pointcut("execution(* kr.flap.domain.data.DatabaseSeederService.connectToSubProductToCategory(..))")
  public void connectToSubProductToCategoryExecution() {}

  @Around("seedMethodExecution() || connectUserToUserAddressExecution() || connectUserToCartExecution() || connectUserToReserveExecution() || connectUserToOrderExecution() || connectOrderToProductExecution() || connectOrderToDeliveryExecution() || connectProductToCartExecution() || connectProductToSellerExecution() || connectProductToStorageExecution() || connectToSubProductToProductExecution() || connectToSubProductToCategoryExecution()")
  public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object proceed = joinPoint.proceed(); // Execute the method
    long executionTime = System.currentTimeMillis() - start;
    Duration duration = Duration.ofMillis(executionTime);
    long hours = duration.toHours();
    long minutes = duration.toMinutesPart();
    long seconds = duration.toSecondsPart();
    String methodName = joinPoint.getSignature().getName(); // Method name being executed
    String className = joinPoint.getSignature().getDeclaringTypeName(); // Class name of the method
    logger.info(String.format("%s.%s executed in %02d:%02d:%02d", className, methodName, hours, minutes, seconds));
    return proceed;
  }

}
