package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        CategoryRepository categoryRepository = new CategoryRepositoryJdbc();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByUserNameAndCategoryName(spend.getUsername(), spend.getCategory().getCategory());

        try (Connection conn = spendDs.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    """
                            INSERT INTO spend (username, spend_date, currency, amount, description, category_id)
                            VALUES (?,?,?,?,?,?)""", PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, spend.getUsername());
            ps.setDate(2, new Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, categoryEntity.get().getId());

            ps.executeUpdate();

            UUID spendId;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    spendId = UUID.fromString(keys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t find id");
                }
            }
            spend.setId(spendId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spend;
    }
}
