package kr.flap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  @Value("${TOSS_PAYMENT_CLIENT_SECRET_KEY}")
  private String clientKey;

  @ModelAttribute("clientKey")
  public String clientKey() {
    return clientKey;
  }
}
