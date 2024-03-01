package kr.flap.domain.model.product;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.cart.CartProductRepository;
import kr.flap.domain.model.cart.CartRepository;
import kr.flap.domain.model.order.*;
import kr.flap.domain.model.product.enums.StorageType;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import static kr.flap.factory.FakeDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest()
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductTest {

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
  @DisplayName("Product Update, Delete 테스트")
  class ProductUDTest {

    @Test
    @DisplayName("Product Update 테스트")
    void productUpdate() {

      List<Product> products = productRepository.findAll();
      BigInteger changeProductId = products.get(0).getId();
      String beforeUpdatedTime = products.get(0).getUpdatedDate();
      products.get(0).setProductNo(BigInteger.valueOf(9999));
      products.get(0).setShortDescription("바뀐 상품명");
      products.get(0).setExpirationDate(LocalDate.of(9999,12,31));
      products.get(0).setMainImageUrl("바뀐 이미지 URL");

      productRepository.flush();

      Optional<Product> changedProduct = productRepository.findById(changeProductId);
      assertThat(changedProduct.get().getProductNo()).isEqualTo(BigInteger.valueOf(9999));
      assertThat(changedProduct.get().getShortDescription()).isEqualTo("바뀐 상품명");
      assertThat(changedProduct.get().getExpirationDate()).isEqualTo(LocalDate.of(9999,12,31));
      assertThat(changedProduct.get().getMainImageUrl()).isEqualTo("바뀐 이미지 URL");
    }

    @Test
    @DisplayName("Product Delete 테스트")
    void productDelete() {
      List<Product> products = productRepository.findAll();
      BigInteger deleteProductId = products.get(0).getId();
      productRepository.delete(products.get(0));

      assertThatThrownBy(() -> productRepository.findById(deleteProductId).orElseThrow())
              .isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("SubProduct Update, Delete 테스트")
  class SubProductUDTest {

    @Test
    @DisplayName("SubProduct Update 테스트")
    void subProductUpdate() {
      List<SubProduct> subProducts = subProductRepository.findAll();
      BigInteger changeSubProductId = subProducts.get(0).getId();
      subProducts.get(0).setName("바뀐 상품명");
      subProducts.get(0).setBrand("바뀐 브랜드");
      subProducts.get(0).setTag((Map<String, Object>) new HashMap<>().put("changedKey", "changedValue"));
      subProducts.get(0).setBasePrice(BigDecimal.valueOf(9999));
      subProducts.get(0).setRetailPrice(BigDecimal.valueOf(9999));
      subProducts.get(0).setDiscountRate(9999);
      subProducts.get(0).setDiscountPrice(BigDecimal.valueOf(9999));
      subProducts.get(0).setRestock(9999);
      subProducts.get(0).setCanRestockNotify(true);
      subProducts.get(0).setMinQuantity(9999);
      subProducts.get(0).setMaxQuantity(9999);
      subProducts.get(0).setIsSoldOut(true);
      subProducts.get(0).setIsPurchaseStatus(true);


      subProductRepository.flush();

      Optional<SubProduct> changedSubProduct = subProductRepository.findById(changeSubProductId);
      assertThat(changedSubProduct.get().getName()).isEqualTo("바뀐 상품명");
      assertThat(changedSubProduct.get().getBrand()).isEqualTo("바뀐 브랜드");
      assertThat(changedSubProduct.get().getTag()).isEqualTo(new HashMap<>().put("changedKey", "changedValue"));
      assertThat(changedSubProduct.get().getBasePrice()).isEqualTo(BigDecimal.valueOf(9999));
      assertThat(changedSubProduct.get().getRetailPrice()).isEqualTo(BigDecimal.valueOf(9999));
      assertThat(changedSubProduct.get().getDiscountRate()).isEqualTo(9999);
      assertThat(changedSubProduct.get().getDiscountPrice()).isEqualTo(BigDecimal.valueOf(9999));
      assertThat(changedSubProduct.get().getRestock()).isEqualTo(9999);
      assertThat(changedSubProduct.get().getCanRestockNotify()).isEqualTo(true);
      assertThat(changedSubProduct.get().getMinQuantity()).isEqualTo(9999);
      assertThat(changedSubProduct.get().getMaxQuantity()).isEqualTo(9999);
      assertThat(changedSubProduct.get().getIsSoldOut()).isEqualTo(true);
      assertThat(changedSubProduct.get().getIsPurchaseStatus()).isEqualTo(true);

    }

    @Test
    @DisplayName("SubProduct Delete 테스트")
    void subProductDelete() {
      List<SubProduct> subProducts = subProductRepository.findAll();
      BigInteger deleteSubProductId = subProducts.get(0).getId();
      subProductRepository.delete(subProducts.get(0));

      assertThatThrownBy(() -> subProductRepository.findById(deleteSubProductId).orElseThrow())
              .isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("Storage Update, Delete 테스트")
  class StorageUDTest {

    @Test
    @DisplayName("Storage Update 테스트")
    void storageUpdate() {
      List<Storage> storages = storageRepository.findAll();
      BigInteger changeStorageId = storages.get(0).getId();
      storages.get(0).setType(StorageType.AMBIENT);

      storageRepository.flush();

      Optional<Storage> changedStorage = storageRepository.findById(changeStorageId);
      assertThat(changedStorage.get().getType()).isEqualTo(StorageType.AMBIENT);
    }

    @Test
    @DisplayName("Storage Delete 테스트")
    void storageDelete() {
      List<Storage> storages = storageRepository.findAll();
      BigInteger deleteStorageId = storages.get(0).getId();
      storageRepository.delete(storages.get(0));

      assertThatThrownBy(() -> storageRepository.findById(deleteStorageId).orElseThrow())
              .isInstanceOf(NoSuchElementException.class);
    }
  }
}
