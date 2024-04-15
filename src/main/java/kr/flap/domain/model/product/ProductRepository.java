package kr.flap.domain.model.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, BigInteger> {

  @Query("SELECT p FROM Product p join fetch p.seller join fetch p.storage join fetch p.subProducts")
  List<Product> findAll();

  @EntityGraph(attributePaths = {"seller", "storage", "subProducts"})
  Page<Product> findAll(Pageable  pageable);
}
