package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class FriendsApiClient extends RestClient {

    private final FriendsApi friendsApi;

    public FriendsApiClient() {
        super(Config.getInstance().frontUrl());
        friendsApi = retrofit.create(FriendsApi.class);
    }

    @Step("Получить список друзей пользователя: {username}")
    public List<UserJson> friends(String username, boolean includePending) throws IOException {
        return friendsApi.friends(username, includePending).execute().body();
    }

    @Step("Получить список приглашений пользователя: {username}")
    public List<UserJson> invitations(String username) throws IOException {
        return friendsApi.invitations(username).execute().body();
    }
}
