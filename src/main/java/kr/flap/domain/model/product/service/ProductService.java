package kr.flap.domain.model.product.service;

import kr.flap.domain.model.product.*;
import kr.flap.domain.model.product.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
  private final ProductImageRepository productImageRepository;
  private final ImageUploadService imageUploadService;
  private final NaverCloudService naverCloudService;
  private final ResourceLoader resourceLoader;

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

  public Product createProduct(ProductCreateDto productDto, SellerDto sellerDto,
                               StorageDto storageDto, List<SubProductCreateDto> subProductDtos, List<MultipartFile> images) throws IOException {
    Seller seller = Seller.builder().name(sellerDto.getName())
            .build();
    sellerRepository.save(seller);

    Storage storage = Storage.builder().type(storageDto.getType())
            .build();

    storageRepository.save(storage);

    List<ImageUploadResponse> imageUploadResponses = images.stream()
            .map(file -> {
              try {
                return naverCloudService.uploadImage(file);
              } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
              }
            })
            .collect(Collectors.toList());

    String mainImageUrl = imageUploadResponses.isEmpty() ? null : imageUploadResponses.get(0).getObjectUrl();

    Product product = Product.builder().shortDescription(productDto.getShortDescription())
            .expirationDate(productDto.getExpirationDate())
            .mainImageUrl(mainImageUrl)
            .seller(seller)
            .storage(storage)
            .build();
    productRepository.save(product);

    List<ProductImage> productImages = imageUploadResponses.stream()
            .map(response -> ProductImage.builder()
                    .product(product)
                    .imageUrl(response.getObjectUrl())
                    .eTag(response.getETag())
                    .build())
            .collect(Collectors.toList());

    productImageRepository.saveAll(productImages);

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

  public Product createTestProduct(ProductCreateDto productDto, SellerDto sellerDto, StorageDto storageDto, List<SubProductCreateDto> subProductDtos) throws IOException {
    Seller seller = Seller.builder().name(sellerDto.getName()).build();
    sellerRepository.save(seller);

    Storage storage = Storage.builder().type(storageDto.getType()).build();
    storageRepository.save(storage);

    // 리소스 로더를 사용하여 JAR 내부의 리소스를 읽습니다.
    String[] imagePaths = {
            "classpath:image/architecture.png",
            "classpath:image/market-early-erd-v3.png",
            "classpath:image/new_architecture.png"
    };

    List<MultipartFile> mockImages = new ArrayList<>();
    for (String imagePath : imagePaths) {
      Resource resource = resourceLoader.getResource(imagePath);
      String fileName = resource.getFilename();
      try (InputStream inputStream = resource.getInputStream()) {
        byte[] content = inputStream.readAllBytes();
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/png", content);
        mockImages.add(multipartFile);
      } catch (IOException e) {
        log.error("Failed to read image file: {}", imagePath, e);
        throw e;
      }
    }

    // 비동기로 이미지 업로드 처리
    List<CompletableFuture<ImageUploadResponse>> futures = mockImages.stream()
            .map(imageUploadService::uploadImageAsync)
            .collect(Collectors.toList());

    // 메인 트랜잭션에서는 기본 Product 정보와 SubProduct 정보만 저장
    Product product = saveProductBaseInfo(productDto, seller, storage);
    product.setSubProducts(saveSubProducts(subProductDtos, product));

    // 비동기 작업 완료 후 후속 작업을 처리합니다.
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenAccept(v -> {
              List<ImageUploadResponse> imageUploadResponses = futures.stream()
                      .map(CompletableFuture::join)
                      .collect(Collectors.toList());

              updateProductWithImages(product, imageUploadResponses);
            }).exceptionally(ex -> {
              log.error("Failed to create product and related entities", ex);
              return null;
            });

    return product;
  }

  public Product saveProductBaseInfo(ProductCreateDto productDto, Seller seller, Storage storage) {
    Product product = Product.builder().shortDescription(productDto.getShortDescription())
            .expirationDate(productDto.getExpirationDate())
            .seller(seller)
            .storage(storage)
            .build();
    productRepository.save(product);
    return product;
  }

  public List<SubProduct> saveSubProducts(List<SubProductCreateDto> subProductDtos, Product product) {
    List<SubProduct> returnSubProductList = new ArrayList<>();
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
      returnSubProductList.add(subProductRepository.save(subProduct));
    });
    return returnSubProductList;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateProductWithImages(Product product, List<ImageUploadResponse> imageUploadResponses) {
    String mainImageUrl = imageUploadResponses.isEmpty() ? null : imageUploadResponses.get(0).getObjectUrl();
    product.setMainImageUrl(mainImageUrl);
    productRepository.save(product);

    List<ProductImage> productImages = imageUploadResponses.stream()
            .map(response -> ProductImage.builder()
                    .product(product)
                    .imageUrl(response.getObjectUrl())
                    .eTag(response.getETag())
                    .build())
            .collect(Collectors.toList());

    productImageRepository.saveAll(productImages);

    log.info("Product and related entities saved successfully");
  }
}
