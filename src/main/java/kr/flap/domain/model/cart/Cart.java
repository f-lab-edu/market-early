package kr.flap.domain.model.cart;

import jakarta.persistence.*;
import kr.flap.domain.model.user.User;
import kr.flap.domain.model.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private BigInteger id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;

  @Builder
  public Cart(User user) {
    this.user = user;
  }

  public void setUserCart(User user) {
    this.user = user;
    user.setCart(this);
  }
}
