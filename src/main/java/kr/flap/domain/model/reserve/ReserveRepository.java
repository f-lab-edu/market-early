package kr.flap.domain.model.reserve;

import kr.flap.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, BigInteger> {

}
