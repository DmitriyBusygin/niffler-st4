package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Optional;

import static guru.qa.niffler.db.Database.SPEND;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {

    public SpendRepositoryHibernate() {
        super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        CategoryRepository categoryRepository = new CategoryRepositoryJdbc();
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByUserNameAndCategoryName(spend.getUsername(), spend.getCategory().getCategory());
        spend.setCategory(categoryEntity.get());

        persist(SPEND, spend);
        return spend;
    }
}
