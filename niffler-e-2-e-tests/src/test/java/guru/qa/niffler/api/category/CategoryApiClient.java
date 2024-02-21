package guru.qa.niffler.api.category;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(Config.getInstance().frontUrl());
        categoryApi = retrofit.create(CategoryApi.class);
    }

    @Step("Добавить категорию: {addCategory}")
    public CategoryJson addCategory(CategoryJson category) throws IOException {
        return categoryApi.addCategory(category).execute().body();
    }

    @Step("Получить категории пользователя: {username}")
    public List<CategoryJson> getCategories(String username) throws IOException {
        return categoryApi.getCategories(username).execute().body();
    }
}
