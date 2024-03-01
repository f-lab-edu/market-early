package kr.flap.domain.model.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, BigInteger> {

}
