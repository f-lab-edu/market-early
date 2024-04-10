package kr.flap.domain.data;

import kr.flap.domain.model.cart.Cart;
import kr.flap.domain.model.cart.CartProduct;
import kr.flap.domain.model.order.Delivery;
import kr.flap.domain.model.order.Order;
import kr.flap.domain.model.order.OrderProduct;
import kr.flap.domain.model.product.*;
import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.user.UserAddress;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@Transactional
public class DatabaseSeederService {
  private final UserSeeder userSeeder;
  private final UserAddressSeeder userAddressSeeder;
  private final CartSeeder cartSeeder;
  private final ReserveSeeder reserveSeeder;
  private final OrderSeeder orderSeeder;
  private final DeliverySeeder deliverySeeder;
  private final ProductSeeder productSeeder;
  private final OrderProductSeeder orderProductSeeder;
  private final CartProductSeeder cartProductSeeder;
  private final SellerSeeder sellerSeeder;
  private final StorageSeeder storageSeeder;
  private final SubProductSeeder subProductSeeder;
  private final CategorySeeder categorySeeder;

  public void seedDatabase() {
    if(!userSeeder.isDataAlreadySeeded()) {
      userSeeder.seed();
    }

    if(!userAddressSeeder.isDataAlreadySeeded()) {
      userAddressSeeder.seed();
    }

    if(!cartSeeder.isDataAlreadySeeded()) {
      cartSeeder.seed();
    }

    if(!reserveSeeder.isDataAlreadySeeded()) {
      reserveSeeder.seed();
    }

    if (!orderSeeder.isDataAlreadySeeded()) {
      orderSeeder.seed();
    }

    if(!deliverySeeder.isDataAlreadySeeded()) {
      deliverySeeder.seed();
    }

    if(!productSeeder.isDataAlreadySeeded()) {
      productSeeder.seed();
    }

    if(!sellerSeeder.isDataAlreadySeeded()) {
      sellerSeeder.seed();
    }

    if(!storageSeeder.isDataAlreadySeeded()) {
      storageSeeder.seed();
    }

    if(!subProductSeeder.isDataAlreadySeeded()) {
      subProductSeeder.seed();
    }

    if(!categorySeeder.isDataAlreadySeeded()) {
      categorySeeder.seed();
    }
  }

  public boolean isDataAlreadySeeded() {
    return userSeeder.isDataAlreadySeeded() &&
            userAddressSeeder.isDataAlreadySeeded() &&
            cartSeeder.isDataAlreadySeeded() &&
            reserveSeeder.isDataAlreadySeeded() &&
            orderSeeder.isDataAlreadySeeded() &&
            deliverySeeder.isDataAlreadySeeded() &&
            productSeeder.isDataAlreadySeeded() &&
            sellerSeeder.isDataAlreadySeeded() &&
            storageSeeder.isDataAlreadySeeded() &&
            subProductSeeder.isDataAlreadySeeded() &&
            categorySeeder.isDataAlreadySeeded();
  }
  public void connectEntitiesToEntities() {
    connectUserToUserAddress();
    connectUserToCart();
    connectUserToReserve();
    connectUserToOrder();
    connectOrderToProduct();
    connectOrderToDelivery();
    connectProductToCart();
    connectProductToSeller();
    connectProductToStorage();
    connectToSubProductToProduct();
    connectToSubProductToCategory();
  }

  private void connectOrderToDelivery() {
    List<Order> orderList = orderSeeder.getOrderList();
    List<Delivery> deliveryList = deliverySeeder.getDeliveryList();
    for (int i = 0; i < orderList.size(); i++) {
      Order order = orderList.get(i);
      Delivery delivery = deliveryList.get(i);
      order.setDelivery(delivery);
      orderSeeder.setOrder(order);
    }
  }

  private void connectToSubProductToCategory() {
    List<SubProduct> subProductList = subProductSeeder.getSubProductList();
    List<Category> categoryList = categorySeeder.getCategoryList();

    Random random = new Random();

    for (SubProduct subProduct : subProductList) {
      int randomIndex = random.nextInt(categoryList.size());
      Category randomCategory = categoryList.get(randomIndex);
      subProduct.setCategory(randomCategory);
      subProductSeeder.setSubProduct(subProduct);
    }
  }

  private void connectToSubProductToProduct() {
    List<Product> productList = productSeeder.getProductList();
    List<SubProduct> subProductList = subProductSeeder.getSubProductList();
    for (int i = 0; i < productList.size(); i++) {
      Product product = productList.get(i);
      List<SubProduct> setSubProductList = subProductList.subList(i * 2, (i + 1) * 2).stream().peek(subProduct -> subProduct.setProduct(product)).toList();
      subProductSeeder.setSubProductList(setSubProductList);
    }
  }

  private void connectProductToStorage() {
    List<Product> productList = productSeeder.getProductList();
    List<Storage> storageList = storageSeeder.getStorageList();
    for (int i = 0; i < productList.size(); i++) {
      Product product = productList.get(i);
      Storage storage = storageList.get(i);
      product.setStorage(storage);
      productSeeder.setProduct(product);
    }
  }

  private void connectProductToSeller() {
    List<Product> productList = productSeeder.getProductList();
    List<Seller> sellerList = sellerSeeder.getSellerList();
    for (int i = 0; i < productList.size(); i++) {
      Product product = productList.get(i);
      Seller seller = sellerList.get(i);
      product.setSeller(seller);
      productSeeder.setProduct(product);
    }
  }

  private void connectProductToCart() {
    List<Product> productList = productSeeder.getProductList();
    List<Cart> cartList = cartSeeder.getCartList();
    for (int i = 0; i < cartList.size(); i++) {
      Cart cart = cartList.get(i);
      List<Product> productsForCart = productList.subList(i * 3, (i + 1) * 3);
      List<CartProduct> cartProducts = productsForCart.stream().map(product -> CartProduct.builder()
              .cart(cart)
              .product(product)
              .quantity(1)
              .productPrice(BigDecimal.valueOf(100))
              .build()).toList();
      cartProductSeeder.setCartProductList(cartProducts);
    }
  }

  private void connectOrderToProduct() {
    List<Order> orderList = orderSeeder.getOrderList();
    List<Product> productList = productSeeder.getProductList();
    for (int i = 0; i < orderList.size(); i++) {
      Order order = orderList.get(i);
      List<Product> productsForOrder = productList.subList(i * 3, (i + 1) * 3);
      int quantity = i;
      List<OrderProduct> orderProducts = productsForOrder.stream().map(product -> OrderProduct.builder()
              .order(order)
              .product(product)
              .quantity(quantity)
              .build()).toList();
      orderProductSeeder.setOrderProductList(orderProducts);
    }
  }

  private void connectUserToOrder() {
    List<User> userList = userSeeder.getUserList();
    List<Order> orderList = orderSeeder.getOrderList();
    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      List<Order> userForOrder = orderList.subList(i * 3, (i + 1) * 3);
      List<Order> setOrderToUserList = userForOrder.stream().peek(order -> order.setUser(user)).toList();
      orderSeeder.setOrderList(setOrderToUserList);
    }
  }

  private void connectUserToReserve() {
    List<User> userList = userSeeder.getUserList();
    List<Reserve> reserveList = reserveSeeder.getReserveList();
    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      List<Reserve> reservesForUser = reserveList.subList(i * 5, (i + 1) * 5);
      List<Reserve> saveReserveSetUserList = reservesForUser.stream().peek(reserve -> reserve.setUser(user)).toList();
      reserveSeeder.setReserveList(saveReserveSetUserList);
    }
  }

  private void connectUserToCart() {
    List<User> userList = userSeeder.getUserList();
    List<Cart> cartList = cartSeeder.getCartList();

    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      Cart cart = cartList.get(i);
      cart.setUser(user);
      cartSeeder.setCartConnectedUser(cart);
    }
  }

  private void connectUserToUserAddress() {
    List<User> userList = userSeeder.getUserList();
    List<UserAddress> userAddresseList = userAddressSeeder.getUserAddresses();
    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      List<UserAddress> addressesConnectToUser = userAddresseList.subList(i * 5, (i + 1) * 5);
      List<UserAddress> connectedAddressToUser = addressesConnectToUser.stream().peek(address -> address.setUser(user)).toList();
      userAddressSeeder.setUserAddressToUser(connectedAddressToUser);
    }
  }

}