package guru.qa.niffler.api.currency;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class CurrencyApiClient extends RestClient {

    private final CurrencyApi currencyApi;

    public CurrencyApiClient() {
        super(Config.getInstance().frontUrl());
        currencyApi = retrofit.create(CurrencyApi.class);
    }

    @Step("Получить список валют")
    public List<CurrencyJson> getAllCurrencies() throws IOException {
        return currencyApi.getAllCurrencies().execute().body();
    }
}
