package kr.flap.domain.model.order;

import jakarta.transaction.Transactional;
import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.cart.CartProductRepository;
import kr.flap.domain.model.cart.CartRepository;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import kr.flap.domain.model.order.enums.OrderStatus;
import kr.flap.domain.model.product.*;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.reserve.ReserveRepository;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.UserAddressRepository;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.factory.FakeDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kr.flap.factory.FakeDataFactory.*;
import static kr.flap.domain.model.user.enums.UserGrade.*;
import static kr.flap.domain.model.user.enums.UserRole.ADMIN;
import static kr.flap.domain.model.user.enums.UserRole.USER;
import static kr.flap.domain.model.user.enums.UserStatus.ACTIVE;
import static kr.flap.domain.model.user.enums.UserStatus.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest()
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserAddressRepository userAddressRepository;

  @Autowired
  ReserveRepository reserveRepository;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  CartProductRepository cartProductRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  SubProductRepository subProductRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  DeliveryRepository deliveryRepository;

  @Autowired
  OrderProductRepository orderProductRepository;

  @Autowired
  StorageRepository storageRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  SellerRepository sellerRepository;

  public List<User> fixUserData;

  public List<UserAddress> fixUserAddressData;

  public List<Reserve> fixReserveData;

  public List<Order> fixOrderData;

  public List<Delivery> fixDeliveryData;

  public List<OrderProduct> fixOrderProductData;

  public List<Cart> fixCartData;

  public List<CartProduct> fixCartProductData;

  public List<Product> fixProductData;

  public List<SubProduct> fixSubProductData;

  public List<Storage> fixStorageData;

  public List<Category> fixCategoryData;

  public List<Seller> fixSellerData;

  @BeforeEach
  public void setUp() {
    fixUserData = createUserData();
    fixUserAddressData = createAddressData();
    fixReserveData = createReserveData();
    fixOrderData = createOrderData();
    fixDeliveryData = createDeliveryData();
    fixOrderProductData = createOrderProductData();
    fixCartData = createCartData();
    fixCartProductData = createCartProductData();
    fixProductData = createProductData();
    fixSubProductData = createSubProductData();
    fixStorageData = createStorageData();
    fixCategoryData = createCategoryData();
    fixSellerData = createSellerData();

    userRepository.saveAll(fixUserData);
    userAddressRepository.saveAll(fixUserAddressData);
    reserveRepository.saveAll(fixReserveData);
    orderRepository.saveAll(fixOrderData);
    deliveryRepository.saveAll(fixDeliveryData);
    orderProductRepository.saveAll(fixOrderProductData);
    cartRepository.saveAll(fixCartData);
    cartProductRepository.saveAll(fixCartProductData);
    productRepository.saveAll(fixProductData);
    subProductRepository.saveAll(fixSubProductData);
    storageRepository.saveAll(fixStorageData);
    categoryRepository.saveAll(fixCategoryData);
    sellerRepository.saveAll(fixSellerData);

    for (UserAddress userAddress : fixUserAddressData) {
      int randomValue = (int) (Math.random() * 3);
      userAddress.setUser(fixUserData.get(randomValue));
    }

    for (Reserve reserve : fixReserveData) {
      int randomValue = (int) (Math.random() * 3);
      reserve.setUser(fixUserData.get(randomValue));
    }

    for (int i = 0; i < fixOrderData.size(); i++) {
      int randomValue = (int) (Math.random() * 3);
      fixOrderData.get(i).setUser(fixUserData.get(randomValue));
      fixOrderData.get(i).setDelivery(fixDeliveryData.get(i));
    }

    for (int i = 0; i < fixOrderProductData.size(); i++) {
      fixOrderProductData.get(i).setOrder(fixOrderData.get(i));
      fixOrderProductData.get(i).setProduct(fixProductData.get(i));
    }

    for (Cart cart : fixCartData) {
      int randomValue = (int) (Math.random() * 3);
      User user = fixUserData.get(randomValue);
      cart.setUser(user);
      cart.setUserCart(user);
    }

    for (int i = 0; i < fixCartProductData.size(); i++) {
      fixCartProductData.get(i).setCart(fixCartData.get(i));
      fixCartProductData.get(i).setProduct(fixProductData.get(i));
    }

    for (int i = 0; i < fixProductData.size(); i++) {
      int randomValue = (int) (Math.random() * 10);
      fixProductData.get(i).setSeller(fixSellerData.get(randomValue));
      fixProductData.get(i).setStorage(fixStorageData.get(i));
    }

    for (SubProduct subProduct : fixSubProductData) {
      int randomValue = (int) (Math.random() * 10);
      subProduct.setProduct(fixProductData.get(randomValue));
      subProduct.setCategory(fixCategoryData.get(randomValue));
    }

    for (Delivery delivery : fixDeliveryData) {
      List<Order> orders = orderRepository.findAll();
      List<UserAddress> userAddressList1 = userAddressRepository.findAll();
      Optional<Order> order1 = orders.stream()
              .filter(order -> order.getDelivery().equals(delivery))
              .findFirst();

      order1.ifPresentOrElse(order -> {
        userAddressList1.stream().filter(userAddress -> userAddress.getUser().equals(order.getUser()))
                .findFirst().ifPresent(delivery::setAddress);
      }, () -> {
        throw new IllegalArgumentException("주문이 없습니다.");
      });
    }

    List<UserAddress> userAddressList = userAddressRepository.findAll();
    userAddressList.forEach(userAddress -> {
      assertThat(userAddress.getUser()).isNotNull();
    });
  }

  @Test
  @DisplayName("BeforeEach로 만든 데이터 개수 확인")
  public void checkSetUp() {
    List<User> userList = userRepository.findAll();
    List<UserAddress> userAddressList = userAddressRepository.findAll();
    List<Reserve> reserveList = reserveRepository.findAll();
    List<Cart> carts = cartRepository.findAll();
    List<Order> orders = orderRepository.findAll();
    List<Delivery> deliveries = deliveryRepository.findAll();
    List<CartProduct> cartProducts = cartProductRepository.findAll();
    List<OrderProduct> orderProducts = orderProductRepository.findAll();
    List<Product> products = productRepository.findAll();
    List<SubProduct> subProducts = subProductRepository.findAll();
    List<Storage> storages = storageRepository.findAll();
    List<Category> categories = categoryRepository.findAll();

    assertThat(userList.size()).isEqualTo(3);
    assertThat(userAddressList.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(reserveList.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(carts.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(orders.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(deliveries.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(cartProducts.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(orderProducts.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(products.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(subProducts.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(storages.size()).isEqualTo(FakeDataFactory.numberOfData);
    assertThat(categories.size()).isEqualTo(FakeDataFactory.numberOfData);

  }

  @Test
  @DisplayName("유저 먼저 생성")
  public void checkUserData() {
    List<User> userList = userRepository.findAll();
    Long userId1 = userList.get(0).getId();
    Long userId2 = userList.get(1).getId();
    Long userId3 = userList.get(2).getId();

    //when
    User user1 = userRepository.findById(BigInteger.valueOf(userId1)).orElseThrow(() -> new IllegalArgumentException("user1 유저가 없습니다."));
    User user2 = userRepository.findById(BigInteger.valueOf(userId2)).orElseThrow(() -> new IllegalArgumentException("user2 유저가 없습니다."));
    User user3 = userRepository.findById(BigInteger.valueOf(userId3)).orElseThrow(() -> new IllegalArgumentException("user3 유저가 없습니다."));

    assertThat(user1).isNotNull();
    assertThat(user2).isNotNull();
    assertThat(user3).isNotNull();
  }

  @Nested
  @DisplayName("주문 CRUD 테스트(CR은 이미 setup에서 테스트 완료)")
  public class OrderCRUDTest {

    @Test
    @DisplayName("주문을 업데이트 할때")
    void updateOrder() {
      List<Order> orders = orderRepository.findAll();
      assertThat(orders.size()).isEqualTo(numberOfData);
      orders.forEach(order -> {
        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);
      });

      orderRepository.flush();

      List<Order> updatedOrders = orderRepository.findAll();
      updatedOrders.forEach(order -> {
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETE);
      });
    }

    @Test
    @DisplayName("주문을 삭제할때")
    void deleteOrder() {
      List<Order> orders = orderRepository.findAll();
      List<OrderProduct> orderProducts = orderProductRepository.findAll();
      assertThat(orders.size()).isEqualTo(numberOfData);
      assertThat(orderProducts.size()).isEqualTo(numberOfData);

      orderProducts.stream().filter(orderProduct -> orderProduct.getOrder().equals(orders.get(0)))
              .forEach(orderProduct -> orderProductRepository.delete(orderProduct));

      Order order1 = orders.get(0);
      orderRepository.delete(order1);

      List<OrderProduct> afterDeleteOrderProducts = orderProductRepository.findAll();
      List<Order> afterDeleteOrder = orderRepository.findAll();

      assertThat(afterDeleteOrderProducts.size()).isEqualTo(numberOfData -1);
      assertThat(afterDeleteOrder.size()).isEqualTo(numberOfData -1);
    }
  }

  @Nested
  @DisplayName("배송 CRUD 테스트(CR은 이미 setup에서 테스트 완료)")
  public class DeliveryCRUDTest {
    @Test
    @DisplayName("배송을 업데이트 할때")
    void updateDelivery() {
      List<Delivery> deliveries = deliveryRepository.findAll();
      assertThat(deliveries.size()).isEqualTo(numberOfData);
      deliveries.forEach(delivery -> {
        delivery.setStatus(DeliveryStatus.COMPLETE);
        deliveryRepository.save(delivery);
      });

      List<Delivery> updatedDeliveries = deliveryRepository.findAll();
      updatedDeliveries.forEach(delivery -> {
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.COMPLETE);
      });
    }

    @Test
    @DisplayName("배송을 삭제할때")
    void deleteDelivery() {
      List<Delivery> deliveries = deliveryRepository.findAll();
      List<OrderProduct> orderProducts = orderProductRepository.findAll();
      List<Order> orders = orderRepository.findAll();
      assertThat(deliveries.size()).isEqualTo(numberOfData);
      assertThat(orders.size()).isEqualTo(numberOfData);

      orderProducts.stream().filter(orderProduct -> orderProduct.getOrder().getDelivery().equals(deliveries.get(0)))
              .forEach(orderProduct -> orderProductRepository.delete(orderProduct));

      orders.stream().filter(order -> order.getDelivery().equals(deliveries.get(0)))
              .forEach(order -> orderRepository.delete(order));

      List<Order> afterDeleteOrders = orderRepository.findAll();
      List<Delivery> afterDeleteDeliveries = deliveryRepository.findAll();

      assertThat(afterDeleteOrders.size()).isEqualTo(numberOfData -1);
      assertThat(afterDeleteDeliveries.size()).isEqualTo(numberOfData -1);

      assertThatThrownBy(() -> deliveryRepository.findById(deliveries.get(0).getId()).orElseThrow(() -> new IllegalArgumentException("배송이 없습니다.")))
              .isInstanceOf(IllegalArgumentException.class);
    }
  }
}