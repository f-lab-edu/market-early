package kr.flap.domain.model.cart;


import kr.flap.domain.model.order.*;
import kr.flap.domain.model.product.*;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.reserve.ReserveRepository;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.UserAddressRepository;
import kr.flap.domain.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kr.flap.factory.FakeDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest()
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartTest {


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

  @Nested
  @DisplayName("Cart와 CartProduct CRUD 중 Update Delete 테스트")
  class cartAndCartProductUDTest {

    @Test
    @DisplayName("CartProduct 삭제후 Cart 삭제")
    void deleteCartProductAndCart() {

      List<CartProduct> cartProducts = cartProductRepository.findAll();
      List<Cart> carts = cartRepository.findAll();

      cartProducts.stream().filter(cartProduct -> cartProduct.getCart().equals(carts.get(0)))
              .findFirst().ifPresent(cartProduct -> {
                cartProductRepository.delete(cartProduct);
                cartRepository.delete(carts.get(0));
              });

      assertThat(cartProductRepository.findAll().size()).isEqualTo(numberOfData - 1);
      assertThat(cartRepository.findAll().size()).isEqualTo(numberOfData - 1);

      assertThatThrownBy(() -> cartProductRepository.findById(cartProducts.get(0).getId()).orElseThrow())
              .isInstanceOf(NoSuchElementException.class);
      assertThatThrownBy(() -> cartRepository.findById(carts.get(0).getId()).orElseThrow())
              .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("CartProduct 수정")
    void updateCartProduct() {
      List<CartProduct> cartProducts = cartProductRepository.findAll();
      CartProduct cartProduct = cartProducts.get(0);
      cartProduct.setQuantity(100);
      cartProductRepository.save(cartProduct);
      assertThat(cartProductRepository.findById(cartProduct.getId()).orElseThrow().getQuantity()).isEqualTo(100);
      assertThat(cartProductRepository.findById(cartProduct.getId()).orElseThrow().getProductPrice()).isEqualTo(cartProduct.getProductPrice());
    }
  }
}
