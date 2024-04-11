package kr.flap.domain.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, BigInteger> {

  public List<UserAddress> findUserAddressByUserId(Long user_id);
}
