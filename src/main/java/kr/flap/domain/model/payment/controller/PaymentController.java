package kr.flap.domain.model.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.flap.domain.model.payment.service.ConfirmService;
import kr.flap.domain.model.product.dto.ProductDto;
import kr.flap.domain.model.product.service.ProductService;
import kr.flap.domain.model.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

 private final ConfirmService confirmService;
 private final ProductService productService;
 private final UserLoginService userLoginService;

  @GetMapping("/payment-info")
  public Map<String, String> getPaymentInfo() {
    Map<String, String> productUserInfo = new HashMap<>();
    // 랜덤한 productId 생성
    int randomProductId = new Random().nextInt(1000) + 1;
    // 해당 productId에 해당하는 Product 정보 가져오기
    ProductDto product = productService.findById(BigInteger.valueOf(randomProductId));
    // User 정보 가져오기 (이 예제에서는 하드코딩된 정보를 사용합니다)
    String userEmail = "customer123@gmail.com";
    String userName = "김토스";
    String userMobilePhone = "01012341234";
    // 정보를 Map에 저장
    productUserInfo.put("orderId", String.valueOf(product.getId()));
//    productUserInfo.put("productName", product.getName());
    productUserInfo.put("userEmail", userEmail);
    productUserInfo.put("userName", userName);
    productUserInfo.put("userMobilePhone", userMobilePhone);
    return productUserInfo;
  }
  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody String jsonBody) throws Exception {
    Map<String, Object> confirm = confirmService.confirm(jsonBody);
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
