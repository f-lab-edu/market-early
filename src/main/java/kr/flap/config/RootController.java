package kr.flap.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Slf4j
@Controller
public class RootController {

  @RequestMapping("/success")
  public void redirectRootToPaymentSuccess(HttpServletResponse response) throws IOException {
    response.sendRedirect("/v1/payments/success");
  }

  @RequestMapping("/confirm")
  public void redirectRootToPaymentConfirm(HttpServletResponse response) throws IOException {
    response.sendRedirect("/v1/payments/confirm");
  }

  @RequestMapping("/v1/payments/style.css")
  public void redirectPaymentStyleCssToRootCssHttpServletResponse(HttpServletResponse response) throws IOException {
    response.sendRedirect("/style.css");
  }

  @RequestMapping("/test")
  public void test() {
    log.info("Test Done");
  }
}
