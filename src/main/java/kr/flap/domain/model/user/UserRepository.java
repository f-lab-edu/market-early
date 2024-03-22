package kr.flap.domain.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndPassword(String email, String password);
}
