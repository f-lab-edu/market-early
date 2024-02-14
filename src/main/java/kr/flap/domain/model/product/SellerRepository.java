package kr.flap.domain.model.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SellerRepository extends JpaRepository<Seller, BigInteger> {

}
