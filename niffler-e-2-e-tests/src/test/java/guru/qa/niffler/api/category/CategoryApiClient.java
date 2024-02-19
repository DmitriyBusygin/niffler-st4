package guru.qa.niffler.api.category;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(Config.getInstance().frontUrl());
        categoryApi = retrofit.create(CategoryApi.class);
    }
}
