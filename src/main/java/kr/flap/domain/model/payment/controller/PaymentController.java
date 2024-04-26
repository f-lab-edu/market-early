package kr.flap.domain.model.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.flap.domain.model.payment.service.PaymentService;
import kr.flap.domain.model.product.dto.ProductDto;
import kr.flap.domain.model.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

  private final PaymentService paymentService;
  private final ProductService productService;

  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody String jsonBody) throws Exception {
    Map<String, Object> confirm = paymentService.confirm(jsonBody);
    int code = (Integer) confirm.get("code");
    return ResponseEntity.status(code).body(confirm);
  }

  /**
   * 인증성공처리
   *
   * @param request
   * @param model
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/success", method = RequestMethod.GET)
  public String paymentRequest(HttpServletRequest request, Model model) throws Exception {
    return "/success";
  }

  @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
  public String index(HttpServletRequest request, Model model) throws Exception {
    int randomProductId = new Random().nextInt(1000) + 1;

    ProductDto product = productService.findById(BigInteger.valueOf(randomProductId));
    String productName = product.getSubProducts().get(0).getName();

    Model paymentInfo = paymentService.getPaymentInfo(model);

    paymentInfo.addAttribute("productName", productName);

    return "/checkout";
  }

  /**
   * 인증실패처리
   *
   * @param request
   * @param model
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/fail", method = RequestMethod.GET)
  public String failPayment(HttpServletRequest request, Model model) throws Exception {
    String failCode = request.getParameter("code");
    String failMessage = request.getParameter("message");

    model.addAttribute("code", failCode);
    model.addAttribute("message", failMessage);

    return "/fail";
  }
}
