package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.CategoryEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class CategoryRepositoryJdbc implements CategoryRepository {

    private final DataSource categoryDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

    @Override
    public Optional<CategoryEntity> findByUserNameAndCategoryName(String userName, String categoryName) {
        CategoryEntity categoryEntity = new CategoryEntity();
        try (Connection conn = categoryDs.getConnection();
             PreparedStatement categoryPs = conn.prepareStatement("""
                    SELECT * FROM category WHERE category.category = ? and username = ?""")) {
            categoryPs.setObject(1, categoryName);
            categoryPs.setObject(2, userName);
            categoryPs.execute();
            try (ResultSet resultSet = categoryPs.getResultSet()) {
                if (resultSet.next()) {
                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setCategory(resultSet.getString("category"));
                    categoryEntity.setUsername(resultSet.getString("username"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(categoryEntity);
    }
}
