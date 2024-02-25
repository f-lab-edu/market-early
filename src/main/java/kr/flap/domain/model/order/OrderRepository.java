package kr.flap.domain.model.order;

import kr.flap.domain.model.reserve.Reserve;
import kr.flap.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, BigInteger> {
  List<Order> findByUser(User user);
}
