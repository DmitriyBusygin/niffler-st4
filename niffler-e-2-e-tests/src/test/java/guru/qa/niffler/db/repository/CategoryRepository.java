package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository {

    Optional<CategoryEntity> findByUserNameAndCategoryName(String userName, String categoryName);
}
