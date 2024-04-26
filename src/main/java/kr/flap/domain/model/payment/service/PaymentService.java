package kr.flap.domain.model.payment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.DeliveryRepository;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.order.OrderRepository;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import kr.flap.domain.model.order.enums.OrderStatus;
import kr.flap.domain.model.product.dto.ProductDto;
import kr.flap.domain.model.product.service.ProductService;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

  @Value("${TOSS_PAYMENT_TEST_SECRET_KEY}")
  private String widgetSecretKey;

  @Value("${TOSS_PAYMENT_TEST_CONFIRM_URL}")
  private String confirmUrl;

  private final ProductService productService;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final DeliveryRepository deliveryRepository;

  private final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
  private final int MIN_LENGTH = 6;
  private final int MAX_LENGTH = 64;
  private final Random RANDOM = new Random();

  public Map<String, Object> confirm(String jsonBody) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> requestData = objectMapper.readValue(jsonBody, Map.class);

    String paymentKey = (String) requestData.get("paymentKey");
    String orderId = (String) requestData.get("orderId");
    String amount = (String) requestData.get("amount");

    Map<String, Object> obj = new HashMap<>();
    obj.put("orderId", orderId);
    obj.put("amount", Integer.parseInt(amount));
    obj.put("paymentKey", paymentKey);

    // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
    // @docs https://docs.tosspayments.com/reference/using-api/api-keys

    // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
    // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
    // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
    Base64.Encoder encoder = Base64.getEncoder();
    byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
    String authorizations = "Basic " + new String(encodedBytes);

    // 결제 승인 API를 호출하세요.
    // 결제를 승인하면 결제수단에서 금액이 차감돼요.
    // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
    URL url = new URL(confirmUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Authorization", authorizations);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);


    OutputStream outputStream = connection.getOutputStream();

    String json = objectMapper.writeValueAsString(obj);
    outputStream.write(json.getBytes(StandardCharsets.UTF_8));

    int code = connection.getResponseCode();
    boolean isSuccess = code == 200;

    InputStream responseStream;
    String responseString = "";
    if (code == 200) {
      //TODO: 결제 성공시 처리
      responseStream = connection.getInputStream();

      ObjectMapper SuccessobjectMapper = new ObjectMapper();

      // Convert InputStream to String
      responseString = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);

     // Parse the JSON string to a Map
      Map<String, Object> responseMap = SuccessobjectMapper.readValue(responseString, new TypeReference<Map<String, Object>>() {
      });

      for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
        log.info("key: [{}], value: [{}]", entry.getKey(), entry.getValue());
      }
      // Extract the values
      String InputPaymentKey = (String) responseMap.get("paymentKey");
      String InputOrderId = (String) responseMap.get("orderId");
      int InputAmount = (Integer) responseMap.get("totalAmount");
      String email = "test@example.com";
      String password = "password!@#$P";
      User mockUser = User.builder().email(email)
              .grade(UserGrade.BRONZE)
              .username("testUser1")
              .gender(UserGender.MALE)
              .birthday(LocalDate.parse("1991-01-01"))
              .role(UserRole.USER)
              .status(UserStatus.ACTIVE)
              .mobileNumber("010-1111-1111")
              .nickname("testUser1")
              .password(password)
              .build();

      userRepository.save(mockUser);

      Delivery mockDelivery = Delivery.builder()
              .status(DeliveryStatus.SHIPPING)
              .build();

      deliveryRepository.save(mockDelivery);

      Order mockOrder = Order.builder()
              .paymentOrderId(InputOrderId)
              .amount(InputAmount)
              .paymentKey(InputPaymentKey)
              .status(OrderStatus.CANTORDER)
              .user(mockUser)
              .delivery(mockDelivery)
              .build();

      orderRepository.save(mockOrder);

    } else {
      //TODO: 결제 실패시 처리
      responseStream = connection.getErrorStream();
      log.info("code: [{}] 결제 실패", code);

      // 실패 시에도 responseString 생성
      responseString = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
    Map<String, Object> responseMap;
    try {
      responseMap = objectMapper.readValue(responseString, Map.class);
      responseMap.put("code", code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return responseMap;
  }

  @Transactional(readOnly = true)
  public Model getPaymentInfo(Model model) {
    Map<String, String> productUserInfo = new HashMap<>();

    int randomProductId = new Random().nextInt(1000) + 1;
    ProductDto product = productService.findById(BigInteger.valueOf(randomProductId));
    String productName = product.getSubProducts().get(0).getName();
    String randomOrderId = generateRandomOrderId();

    //TODO: Jwt 작업이 완료휴 Merge되면 User쪽 데이터 연계할 예정
    String userEmail = "customer123@gmail.com";
    String userName = "김토스";
    String userMobilePhone = "01012341234";

    productUserInfo.put("productName", productName);
    productUserInfo.put("orderId", randomOrderId);
    productUserInfo.put("userEmail", userEmail);
    productUserInfo.put("userName", userName);
    productUserInfo.put("userMobilePhone", userMobilePhone);

    model.addAttribute("productUserInfo", productUserInfo);

    return model;
  }

  public String generateRandomOrderId() {
    int length = RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = RANDOM.nextInt(ALLOWED_CHARACTERS.length());
      char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
      sb.append(randomChar);
    }
    return sb.toString();
  }
}
