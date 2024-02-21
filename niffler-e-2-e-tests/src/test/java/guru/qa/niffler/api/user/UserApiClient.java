package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient() {
        super(Config.getInstance().frontUrl());
        userApi = retrofit.create(UserApi.class);
    }

    @Step("Обновить данные пользователя: {user}")
    public UserJson updateUserInfo(UserJson user) throws IOException {
        return userApi.updateUserInfo(user).execute().body();
    }

    @Step("Получить данные пользователя: {user}")
    public UserJson currentUser(String username) throws IOException {
        return userApi.currentUser(username).execute().body();
    }
}
