package kr.flap.domain.model.product.controller;

import jakarta.validation.Valid;
import kr.flap.domain.model.product.Product;
import kr.flap.domain.model.product.dto.*;
import kr.flap.domain.model.product.service.ProductService;
import kr.flap.domain.model.product.validation.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
  @PostMapping( consumes = {"application/json", "multipart/form-data"})
  public ResponseEntity<ProductDto> createProduct(
          @RequestPart("product") @Valid ProductCreateDto productDto,
          @RequestPart("seller") @Valid SellerDto sellerDto,
          @RequestPart("storage") @Valid StorageDto storageDto,
          @RequestPart("subProducts") @Valid List<SubProductCreateDto> subProductDtos,
          @RequestPart("images") @Valid List<MultipartFile> images) throws IOException {
    Product product = productService.createProduct(productDto, sellerDto, storageDto, subProductDtos, images);
    ProductDto createdProduct = new ProductDto(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
  @PostMapping(value = "/imageTest", consumes = {"application/json", "multipart/form-data"})
  public ResponseEntity<ProductDto> createTestProduct(
          @RequestPart("product") @Valid ProductCreateDto productDto,
          @RequestPart("seller") @Valid SellerDto sellerDto,
          @RequestPart("storage") @Valid StorageDto storageDto,
          @RequestPart("subProducts") @Valid List<SubProductCreateDto> subProductDtos) throws IOException {
    Product product = productService.createTestProduct(productDto, sellerDto, storageDto, subProductDtos);
    ProductDto createdProduct = new ProductDto(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @PostMapping(value = "/test", consumes = {"multipart/form-data"})
  public ResponseEntity<String> handleFileUpload(@RequestPart("files") List<MultipartFile> files,
                                                 @RequestPart("description") String description) {
    StringBuilder fileNames = new StringBuilder();
    for (MultipartFile file : files) {
      fileNames.append(file.getOriginalFilename()).append(", ");
    }
    return ResponseEntity.ok("Files uploaded successfully: " + fileNames + "description: " + description);
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
