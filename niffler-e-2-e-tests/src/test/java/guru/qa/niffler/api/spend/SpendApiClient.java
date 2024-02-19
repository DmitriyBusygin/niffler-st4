package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(Config.getInstance().frontUrl());
        spendApi = retrofit.create(SpendApi.class);
    }
}
