package kr.flap.domain.model.product.service;

import kr.flap.domain.model.product.*;
import kr.flap.domain.model.product.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final StorageRepository storageRepository;
  private final SubProductRepository subProductRepository;
  public List<ProductDto> findAll() {
    List<Product> products = productRepository.findFetchAll();
    return products.stream().map(ProductDto::new).collect(Collectors.toList());
  }

  public Page<ProductDto> findAll(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);
    return products.map(ProductDto::new);
  }

  @Cacheable(value = "products", key = "#id")
  public ProductDto findById(BigInteger id) {
    Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    return new ProductDto(product);
  }

  public Product createProduct(ProductDto productDto, SellerDto sellerDto, StorageDto storageDto, List<SubProductDto> subProductDtos) {
    Seller seller = Seller.builder().name(sellerDto.getName())
            .build();
    sellerRepository.save(seller);

    Storage storage = Storage.builder().type(storageDto.getType())
            .build();

    storageRepository.save(storage);
    Product product = Product.builder().shortDescription(productDto.getShortDescription())
            .expirationDate(productDto.getExpirationDate())
            .mainImageUrl(productDto.getMainImageUrl())
            .seller(seller)
            .storage(storage)
            .build();
    productRepository.save(product);

    subProductDtos.forEach(subProductDto -> {
      SubProduct subProduct = SubProduct.builder().name(subProductDto.getName())
              .brand(subProductDto.getBrand())
              .tag(subProductDto.getTag())
              .basePrice(subProductDto.getBasePrice())
              .retailPrice(subProductDto.getRetailPrice())
              .discountPrice(subProductDto.getDiscountPrice())
              .discountRate(subProductDto.getDiscountRate())
              .restock(subProductDto.getRestock())
              .canRestockNotify(subProductDto.getCanRestockNotify())
              .minQuantity(subProductDto.getMinQuantity())
              .maxQuantity(subProductDto.getMaxQuantity())
              .isSoldOut(subProductDto.getIsSoldOut())
              .isPurchaseStatus(subProductDto.getIsPurchaseStatus())
              .product(product)
              .build();
      subProductRepository.save(subProduct);
    });

    return productRepository.save(product);
  }

  public Product updateProduct(BigInteger id, ProductUpdateDto productDto) {
    Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    product.setShortDescription(productDto.getShortDescription());
    product.setExpirationDate(productDto.getExpirationDate());
    product.setMainImageUrl(productDto.getMainImageUrl());
    return productRepository.save(product);
  }

  public void deleteProduct(BigInteger id) {
    productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    productRepository.deleteById(id);
  }
}
