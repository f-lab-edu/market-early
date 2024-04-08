package kr.flap.domain.data;

import kr.flap.domain.model.product.Category;
import kr.flap.domain.model.product.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CategorySeeder {

  private final CategoryRepository categoryRepository;

  public Category seed() {
    Category category = Category.builder().name("카테고리").build();

    categoryRepository.save(category);

    return category;
  }
}
