package kr.flap.domain.model.product.controller;

import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.dto.ProductDto;
import kr.flap.domain.model.product.dto.ProductRequestDto;
import kr.flap.domain.model.product.dto.ProductUpdateDto;
import kr.flap.domain.model.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<List<ProductDto>> findAll() {
    List<ProductDto> products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> findById(@PathVariable BigInteger id) {
    ProductDto product = productService.findById(id);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
    Product product = productService.createProduct(productRequestDto.getProduct(), productRequestDto.getSeller(), productRequestDto.getStorage(), productRequestDto.getSubProducts());
    ProductDto createdProduct = new ProductDto(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable BigInteger id, @RequestBody ProductUpdateDto productDto) {
    Product product = productService.updateProduct(id, productDto);
    ProductDto updatedProduct = new ProductDto(product);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BigInteger> deleteProduct(@PathVariable BigInteger id) {
    BigInteger deletedProductId = productService.deleteProduct(id);
    return ResponseEntity.ok(deletedProductId);
  }
}
