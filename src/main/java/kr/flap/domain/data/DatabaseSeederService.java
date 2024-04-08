package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.cart.CartProductRepository;
import kr.flap.domain.model.cart.CartRepository;
import kr.flap.domain.model.order.*;
import kr.flap.domain.model.order.enums.DeliveryStatus;
import kr.flap.domain.model.order.enums.OrderStatus;
import kr.flap.domain.model.product.*;
import kr.flap.domain.model.product.enums.StorageType;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.reserve.ReserveRepository;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.UserAddressRepository;
import kr.flap.domain.model.user.UserRepository;
import kr.flap.domain.model.user.enums.UserGender;
import kr.flap.domain.model.user.enums.UserGrade;
import kr.flap.domain.model.user.enums.UserRole;
import kr.flap.domain.model.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class DatabaseSeederService {

  private final CartRepository cartRepository;
  private final CartProductRepository cartProductRepository;
  private final UserRepository userRepository;
  private final ReserveRepository reserveRepository;
  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final StorageRepository storageRepository;
  private final SubProductRepository subProductRepository;
  private final CategoryRepository categoryRepository;
  private final UserAddressRepository UserAddressRepository;
  private final DeliveryRepository deliveryRepository;
  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;


  static int maxReservesPerUser = 10;

  public boolean isDataAlreadySeeded() {
    return userRepository.count() > 0;
  }


  public void seedDatabase() {

    createCarts();

    createProduct();

    createCardProduct();

    seedReservesForAllUsers(maxReservesPerUser);

    createSeller();

    createStorage();

    createSubProduct();

    createCategory();

    createAddresses();
    createDeliveries();
    createOrders();
    createOrderProducts();
  }

  private void createCategory() {
    for (int i = 1; i < 11; i++) {
      Category category = Category.builder()
              .name("카테고리" + i)
              .build();
      categoryRepository.save(category);
    }
  }

  private void createSubProduct() {
    for (int i = 1; i < 11; i++) {
      productRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("Product not found"));
      SubProduct subProduct = SubProduct.builder()
              .name("상품" + i)
              .brand("브랜드" + i)
              .tag(Map.of("key" + i, "value" + i))
              .basePrice(BigDecimal.valueOf(i * 100))
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
      subProductRepository.save(subProduct);
    }
  }

  private void createStorage() {
    for (int i = 1; i < 11; i++) {
      Storage storage = Storage.builder()
              .type(StorageType.values()[i % StorageType.values().length])
              .build();
      storageRepository.save(storage);
    }
  }

  private void createSeller() {
    for (int i = 1; i < 11; i++) {
      Seller seller = Seller.builder()
              .name("판매자" + i)
              .build();
      sellerRepository.save(seller);
    }
  }

  private void createProduct() {
    for (int i = 1; i < 11; i++) {
      Product product = Product.builder()
              .shortDescription("한글 설명" + i)
              .expirationDate(LocalDate.of(2999, 12, 31))
              .mainImageUrl("https://www.google.com" + i)
              .build();
      productRepository.save(product);
    }
  }

  private void createReserve() {
    for (int i = 1; i < 11; i++) {
        User user = userRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("User not found"));
      for (int j = 1; j < 11; j++) {
        Reserve reserve = Reserve.builder()
                .isValid(true)
                .user(user)
                .amount(BigDecimal.valueOf(j * 100))
                .build();
        reserveRepository.save(reserve);
      }
    }
  }

  private void createCardProduct() {
    for (int i = 1; i < 11; i++) {
      Cart cart = cartRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("Cart not found"));
      Product product = productRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("Product not found"));
      CartProduct cartProduct = CartProduct.builder()
              .cart(cart)
              .product(product)
              .quantity(i)
              .productPrice(BigDecimal.valueOf(i * 100))
              .build();
      cartProductRepository.save(cartProduct);
    }
  }

  private void createCarts() {
    for (int i = 1; i < 11; i++) {
      User user = userRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("User not found"));
      Cart cart = Cart.builder()
              .user(user)
              .build();
      cartRepository.save(cart);
    }
  }


  private void createAddresses() {
    for (int i = 1; i <= 10; i++) {
      UserAddress address = UserAddress.builder()
              .address("주소" + i)
              .addressDetail("상세주소" + i)
              .zipCode("12345" + i)
              .build();
      UserAddressRepository.save(address);
    }
  }

  private void createDeliveries() {
    for (int i = 1; i <= 10; i++) {
      Delivery delivery = Delivery.builder()
              .status(DeliveryStatus.READY)
              .build();
      deliveryRepository.save(delivery);
    }
  }

  private void createOrders() {
    for (int i = 1; i <= 10; i++) {
      User user = userRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("User not found"));
      Order order = Order.builder()
              .user(user)
              .status(OrderStatus.COMPLETE)
              .build();
      orderRepository.save(order);
    }
  }

  private void createOrderProducts() {
    for (int i = 1; i <= 10; i++) {
      Order order = orderRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("Order not found"));
      Product product = productRepository.findById(BigInteger.valueOf(i)).orElseThrow(() -> new RuntimeException("Product not found"));
      OrderProduct orderProduct = OrderProduct.builder()
              .order(order)
              .product(product)
              .quantity(i)
              .build();
      orderProductRepository.save(orderProduct);
    }
  }

  private void seedReservesForAllUsers(int maxReservesPerUser) {
    IntStream.rangeClosed(1, maxReservesPerUser)
            .mapToObj(this::findUserById)
            .forEach(this::createAndSaveReservesForUser);
  }

  private User findUserById(int id) {
    return userRepository.findById(BigInteger.valueOf(id))
            .orElseThrow(() -> new RuntimeException("User not found"));
  }

  private void createAndSaveReservesForUser(User user) {
    IntStream.rangeClosed(1, maxReservesPerUser)
            .mapToObj(i -> createReserve(user, i))
            .forEach(reserveRepository::save);
  }

  private Reserve createReserve(User user, int multiplier) {
    return Reserve.builder()
            .isValid(true)
            .user(user)
            .amount(BigDecimal.valueOf(multiplier * 100))
            .build();
  }
}