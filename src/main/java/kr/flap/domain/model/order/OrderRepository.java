package kr.flap.domain.model.order;

import kr.flap.domain.model.reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface OrderRepository extends JpaRepository<Order, BigInteger> {

}
