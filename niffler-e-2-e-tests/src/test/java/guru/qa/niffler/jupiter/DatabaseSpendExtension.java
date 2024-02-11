package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.repository.SpendRepositoryHibernate;
import guru.qa.niffler.db.repository.SpendRepositoryJdbc;
import guru.qa.niffler.db.repository.SpendRepository;
import guru.qa.niffler.db.repository.SpendRepositorySJdbc;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

    private static final SpendRepository spendRepository = new SpendRepositoryHibernate();

    @Override
    public SpendJson create(SpendJson spend) {
        return spendRepository
                .createSpend(spend.toEntity())
                .toJson();
    }
}
