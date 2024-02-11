package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("category")
        String category) {

    public SpendEntity toEntity() {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(id);
        spendEntity.setUsername(username);
        spendEntity.setSpendDate(spendDate);
        spendEntity.setCurrency(guru.qa.niffler.db.model.CurrencyValues.valueOf(currency.name()));
        spendEntity.setAmount(amount);
        spendEntity.setDescription(description);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(category);
        categoryEntity.setUsername(username);

        spendEntity.setCategory(categoryEntity);

        return spendEntity;
    }
}
