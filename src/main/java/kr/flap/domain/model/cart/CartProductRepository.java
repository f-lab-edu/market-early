package kr.flap.domain.model.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, BigInteger> {

}
