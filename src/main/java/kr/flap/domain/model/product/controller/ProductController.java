package kr.flap.domain.model.product.controller;

import jakarta.validation.Valid;
import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.dto.ProductDto;
import kr.flap.domain.model.product.dto.ProductRequestDto;
import kr.flap.domain.model.product.dto.ProductUpdateDto;
import kr.flap.domain.model.product.service.ProductService;
import kr.flap.domain.model.product.validation.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @GetMapping("/all")
  public ResponseEntity<List<ProductDto>> findAll() {
    List<ProductDto> products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @GetMapping()
  public ResponseEntity<Page<ProductDto>> findAll(Pageable pageable) {
    Page<ProductDto> products = productService.findAll(pageable);
    return ResponseEntity.ok((products));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> findById(@Valid @PathVariable ValidId id) {
    ProductDto product = productService.findById(id.getId());
    return ResponseEntity.ok(product);
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
    Product product = productService.createProduct(productRequestDto.getProduct(), productRequestDto.getSeller(), productRequestDto.getStorage(), productRequestDto.getSubProducts());
    ProductDto createdProduct = new ProductDto(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable ValidId id, @Valid @RequestBody ProductUpdateDto productDto) {
    Product product = productService.updateProduct(id.getId(), productDto);
    ProductDto updatedProduct = new ProductDto(product);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BigInteger> deleteProduct(@Valid @PathVariable ValidId id) {
    productService.deleteProduct(id.getId());
    return ResponseEntity.noContent().build();
  }
}
