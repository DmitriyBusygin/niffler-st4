package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(Config.getInstance().frontUrl());
        spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Добавить spend: {spend}")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }
}
