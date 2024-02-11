package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySJdbc implements SpendRepository {

    private final JdbcTemplate spendTemplate;

    public SpendRepositorySJdbc() {
        JdbcTransactionManager spendTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.dataSource(Database.SPEND));
        this.spendTemplate = new JdbcTemplate(spendTm.getDataSource());
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        CategoryRepository categoryRepository = new CategoryRepositoryJdbc();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByUserNameAndCategoryName(spend.getUsername(), spend.getCategory().getCategory());

        KeyHolder kh = new GeneratedKeyHolder();
        spendTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                            INSERT INTO spend (username, spend_date, currency, amount, description, category_id)
                            VALUES (?,?,?,?,?,?)""",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, categoryEntity.get().getId());

            return ps;
        }, kh);

        spend.setId((UUID) kh.getKeys().get("id"));
        return spend;
    }
}
