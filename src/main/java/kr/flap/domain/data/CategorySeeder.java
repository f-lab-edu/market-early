package kr.flap.domain.data;

import kr.flap.domain.model.product.Category;
import kr.flap.domain.model.product.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Transactional
public class CategorySeeder {

  private final CategoryRepository categoryRepository;

  public void seed() {
    List<Category> categoryList = IntStream.range(1, 101)
            .mapToObj(this::createCategory)
            .toList();

    categoryRepository.saveAll(categoryList);
  }

  public List<Category> getCategoryList() {
    return categoryRepository.findAll();
  }

  private Category createCategory(int i) {
    return Category.builder()
            .name("Category" + i)
            .build();
  }

  public boolean isDataAlreadySeeded() {
    return categoryRepository.count() > 0;
  }
}
