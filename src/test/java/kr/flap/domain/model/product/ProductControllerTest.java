package kr.flap.domain.model.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.flap.domain.model.product.dto.*;
import kr.flap.domain.model.product.enums.StorageType;
import kr.flap.domain.model.product.service.ProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Test
  public void testFindAll() throws Exception {

    List a = new ArrayList();
    // 1. 반환할 ProductDto, SellerDto, StorageDto, SubProductDto 설정
    SellerDto sellerDto = new SellerDto();
    sellerDto.setName("Seller name");

    StorageDto storageDto = new StorageDto();
    storageDto.setType(StorageType.FROZEN);

    SubProductDto subProductDto = new SubProductDto();
    subProductDto.setName("SubProduct name");
    subProductDto.setBrand("SubProduct brand");
    subProductDto.setTag((Map.of("test", "testVal", "test2", "testVal2")));
    subProductDto.setBasePrice(BigDecimal.valueOf(100));
    subProductDto.setRetailPrice(BigDecimal.valueOf(200));
    subProductDto.setDiscountPrice(BigDecimal.valueOf(50));
    subProductDto.setDiscountRate((25));
    subProductDto.setRestock(99);
    subProductDto.setCanRestockNotify(true);
    subProductDto.setMinQuantity(1);
    subProductDto.setMaxQuantity(9999);
    subProductDto.setIsSoldOut(false);
    subProductDto.setIsPurchaseStatus(true);

    List<SubProductDto> subProductDtos = List.of(subProductDto);

    ProductDto productDto = new ProductDto();
    productDto.setId(BigInteger.valueOf(1));
    productDto.setShortDescription("Short description");
    productDto.setExpirationDate(LocalDate.now());
    productDto.setMainImageUrl("http://example.com/image.jpg");
    productDto.setSubProducts(subProductDtos);
    productDto.setStorage(storageDto);
    productDto.setSeller(sellerDto);
    List<ProductDto> productDtoList = List.of(productDto);

    ProductRequestDto productRequestDto = new ProductRequestDto();
    productRequestDto.setProduct(productDto);
    productRequestDto.setSeller(sellerDto);
    productRequestDto.setStorage(storageDto);
    productRequestDto.setSubProducts(subProductDtos);


    when(productService.findAll()).thenReturn(productDtoList);

    // 2. POST /v1/products 요청 시뮬레이션
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    // 4. 응답 본문 확인
    String responseContent = mvcResult.getResponse().getContentAsString();
//    ProductDto responseProductDto = new ObjectMapper().readValue(responseContent, new TypeReference<ProductDto>(){});
    ProductDto responseProductDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(responseContent, ProductDto[].class)[0];
    StorageDto originalStorage = productDto.getStorage();
    SellerDto originalSeller = productDto.getSeller();
    List<SubProductDto> originalSubProducts = productDto.getSubProducts();
    SubProductDto originalSubProductDto = originalSubProducts.get(0);


    assertEquals(productDto.getId(), responseProductDto.getId());
    assertEquals(productDto.getShortDescription(), responseProductDto.getShortDescription());
    assertEquals(productDto.getExpirationDate(), responseProductDto.getExpirationDate());
    assertEquals(productDto.getMainImageUrl(), responseProductDto.getMainImageUrl());
    assertEquals(originalStorage.getType(), responseProductDto.getStorage().getType());
    assertEquals(originalSeller.getName(), responseProductDto.getSeller().getName());
    assertEquals(originalSubProductDto.getName(), responseProductDto.getSubProducts().get(0).getName());
    assertEquals(originalSubProductDto.getBrand(), responseProductDto.getSubProducts().get(0).getBrand());
    assertEquals(originalSubProductDto.getTag(), responseProductDto.getSubProducts().get(0).getTag());
    assertEquals(originalSubProductDto.getBasePrice(), responseProductDto.getSubProducts().get(0).getBasePrice());
    assertEquals(originalSubProductDto.getRetailPrice(), responseProductDto.getSubProducts().get(0).getRetailPrice());
    assertEquals(originalSubProductDto.getDiscountPrice(), responseProductDto.getSubProducts().get(0).getDiscountPrice());
    assertEquals(originalSubProductDto.getDiscountRate(), responseProductDto.getSubProducts().get(0).getDiscountRate());
    assertEquals(originalSubProductDto.getRestock(), responseProductDto.getSubProducts().get(0).getRestock());
    assertEquals(originalSubProductDto.getCanRestockNotify(), responseProductDto.getSubProducts().get(0).getCanRestockNotify());
    assertEquals(originalSubProductDto.getMinQuantity(), responseProductDto.getSubProducts().get(0).getMinQuantity());
    assertEquals(originalSubProductDto.getMaxQuantity(), responseProductDto.getSubProducts().get(0).getMaxQuantity());
    assertEquals(originalSubProductDto.getIsSoldOut(), responseProductDto.getSubProducts().get(0).getIsSoldOut());
    assertEquals(originalSubProductDto.getIsPurchaseStatus(), responseProductDto.getSubProducts().get(0).getIsPurchaseStatus());
  }
}
