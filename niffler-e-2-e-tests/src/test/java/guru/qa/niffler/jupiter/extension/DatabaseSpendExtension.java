package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.repository.SpendRepositoryHibernate;
import guru.qa.niffler.db.repository.SpendRepositoryJdbc;
import guru.qa.niffler.db.repository.SpendRepository;
import guru.qa.niffler.db.repository.SpendRepositorySJdbc;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

    @Override
    public SpendJson create(SpendJson spend) {
        final SpendRepository spendRepository = new SpendRepositoryHibernate();

        return spendRepository
                .createSpend(spend.toEntity())
                .toJson();
    }
}
