package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class UserApiClient extends RestClient {

    private final UserApiClient userApiClient;

    public UserApiClient() {
        super(Config.getInstance().frontUrl());
        userApiClient = retrofit.create(UserApiClient.class);
    }
}
