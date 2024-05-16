package kr.flap.factory;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.order.OrderProduct;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import kr.flap.domain.model.order.enums.OrderStatus;
import kr.flap.domain.model.product.*;
import kr.flap.domain.model.product.enums.StorageType;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import net.datafaker.Faker;
import net.datafaker.providers.base.Options;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FakeDataFactory {

  public static Integer numberOfData = 10;

  public static List<User> createFakeUserData() {
    List<User> userList = new ArrayList<>();
    for (int i = 0; i < numberOfData; i++) {

      Faker faker = new Faker(new Locale("ko"));
      Faker englishFaker = new Faker(new Locale("en"));
      String name = faker.name().fullName().replaceAll("\\s+", "");
      String birthday = faker.date().birthday(1, 99, "YYYY-MM-dd");
      String phoneNumber = "010" + "-" + faker.phoneNumber().subscriberNumber(4) + "-" + faker.phoneNumber().subscriberNumber(4);
      String emailAddress = englishFaker.internet().emailAddress();
      final Options options = faker.options();
      UserGrade userGradeOption = options.option(UserGrade.class);
      UserRole userRoleOption = options.option(UserRole.class);
      UserStatus userStatus = options.option(UserStatus.class);

      User user = User.builder().email(emailAddress)
              .grade(userGradeOption)
              .birthday(LocalDate.parse(birthday))
              .role(userRoleOption)
              .status(userStatus)
              .mobileNumber(phoneNumber)
              .nickname(name)
              .password("testpassword")
              .build();

      userList.add(user);
    }
    return userList;
  }

  public static List<User> createUserData() {
    User user1 = User.builder().email("test1@test.com")
            .grade(UserGrade.BRONZE)
            .gender(UserGender.MALE)
            .username("testUser1")
            .birthday(LocalDate.parse("1991-01-01"))
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .mobileNumber("010-1111-1111")
            .nickname("testUser1")
            .password("testpassword1")
            .build();

    User user2 = User.builder().email("test2@test.com")
            .grade(UserGrade.SILVER)
            .gender(UserGender.FEMALE)
            .username("testUser2")
            .birthday(LocalDate.parse("1992-01-01"))
            .role(UserRole.ADMIN)
            .status(UserStatus.INACTIVE)
            .mobileNumber("010-2222-2222")
            .nickname("testUser2")
            .password("testpassword2")
            .build();


    User user3 = User.builder().email("test3@test.com")
            .grade(UserGrade.GOLD)
            .gender(UserGender.FEMALE)
            .username("testUser3")
            .birthday(LocalDate.parse("1993-01-01"))
            .role(UserRole.USER)
            .status(UserStatus.ACTIVE)
            .mobileNumber("010-3333-3333")
            .nickname("testUser3")
            .password("testpassword3")
            .build();
    ArrayList<User> userArrayList = new ArrayList<>(List.of(user1, user2, user3));
    return userArrayList;
  }

  public static List<UserAddress> createAddressData() {
    List<UserAddress> userAddressList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      UserAddress userAddress = UserAddress.builder()
              .address("서울시 강남구")
              .addressDetail("역삼동 " + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + "-" + String.valueOf(i))
              .zipCode(String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i))
              .build();
      userAddressList.add(userAddress);
    }

    return userAddressList;
  }

  public static List<Reserve> createReserveData() {
    List<Reserve> reserveList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      Reserve reserve = Reserve.builder()
              .isValid(true)
              .amount(BigDecimal.valueOf(numberOfData * i))
              .build();
      reserveList.add(reserve);
    }

    return reserveList;
  }

  public static List<Cart> createCartData() {
    List<Cart> cartList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      Cart cart = Cart.builder()
              .build();

      cartList.add(cart);
    }

    return cartList;
  }

  public static List<CartProduct> createCartProductData() {
    List<CartProduct> cartProductList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      CartProduct cartProduct = CartProduct.builder()
              .quantity(i)
              .productPrice(BigDecimal.valueOf(numberOfData * i))
              .build();

      cartProductList.add(cartProduct);
    }

    return cartProductList;
  }

  public static List<Order> createOrderData() {
    List<Order> orderList = new ArrayList<>();
    OrderStatus[] values = OrderStatus.values();
    for (int i = 0; i < numberOfData; i++) {
      OrderStatus randomOrderStatusValue = values[(int) (Math.random() * values.length)];
      Order order = Order.builder()
              .status(randomOrderStatusValue)
              .build();

      orderList.add(order);
    }

    return orderList;
  }

  public static List<Delivery> createDeliveryData() {
    List<Delivery> deliveryList = new ArrayList<>();
    DeliveryStatus[] values = DeliveryStatus.values();
    for (int i = 0; i < numberOfData; i++) {
      DeliveryStatus deliveryStatus = values[(int) (Math.random() * values.length)];
      Delivery delivery = Delivery.builder()
              .status(deliveryStatus)
              .build();

      deliveryList.add(delivery);
    }

    return deliveryList;
  }

  public static List<OrderProduct> createOrderProductData() {
    List<OrderProduct> orderProductList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      OrderProduct orderProduct = OrderProduct.builder()
              .quantity(i)
              .productPrice(BigDecimal.valueOf(numberOfData * i))
              .build();

      orderProductList.add(orderProduct);
    }

    return orderProductList;
  }

  public static List<Product> createProductData() {
    List<Product> productList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      Product product = Product.builder()
              .shortDescription("한글 설명" + i)
              .expirationDate(LocalDate.of(2999, 12, 31))
              .mainImageUrl("https://www.google.com" + i)
              .build();

      productList.add(product);
    }

    return productList;
  }

  public static List<Seller> createSellerData() {
    List<Seller> sellerList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      Seller seller = Seller.builder()
              .name("판매자" + i)
              .build();

      sellerList.add(seller);
    }

    return sellerList;
  }

  public static List<Storage> createStorageData() {
    List<Storage> storageList = new ArrayList<>();
    StorageType[] values = StorageType.values();
    for (int i = 0; i < numberOfData; i++) {
      StorageType storageType = values[(int) (Math.random() * values.length)];
      Storage storage = Storage.builder()
              .type(storageType)
              .build();

      storageList.add(storage);
    }

    return storageList;
  }

  public static List<SubProduct> createSubProductData() {
    List<SubProduct> subProductList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      SubProduct subProduct = SubProduct.builder()
              .name("상품" + i)
              .brand("브랜드" + i)
              .tag(Map.of("key" + i, "value" + i))
              .basePrice(BigDecimal.valueOf(numberOfData * i))
              .retailPrice(BigDecimal.valueOf(1000 * i))
              .discountRate(i)
              .discountPrice(BigDecimal.valueOf(100 * i))
              .restock(i)
              .canRestockNotify(true)
              .minQuantity(i)
              .maxQuantity(2 * i)
              .isSoldOut(false)
              .isPurchaseStatus(true)
              .build();

      subProductList.add(subProduct);
    }

    return subProductList;
  }

  public static List<Category> createCategoryData() {
    List<Category> categoryList = new ArrayList<>();

    for (int i = 0; i < numberOfData; i++) {
      Category category = Category.builder()
              .name("카테고리" + i)
              .build();

      categoryList.add(category);
    }

    return categoryList;
  }
}

