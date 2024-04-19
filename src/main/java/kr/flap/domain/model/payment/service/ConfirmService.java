package kr.flap.domain.model.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmService {

  @Value("${TOSS_PAYMENT_TEST_SECRET_KEY}")
  private String widgetSecretKey;

  @Value("${TOSS_PAYMENT_TEST_CONFIRM_URL}")
  private String confirmUrl;

  public Map<String, Object> confirm(String jsonBody) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> requestData = objectMapper.readValue(jsonBody, Map.class);

    String paymentKey = (String) requestData.get("paymentKey");
    String orderId = (String) requestData.get("orderId");
    String amount = (String) requestData.get("amount");

    Map<String, Object> obj = new HashMap<>();
    obj.put("orderId", orderId);
    obj.put("amount",Integer.parseInt(amount));
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

    InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

    // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
    Map<String, Object> responseMap;
    try {
      responseMap = objectMapper.readValue(responseStream, Map.class);
      responseMap.put("code", code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return responseMap;
  }
}
