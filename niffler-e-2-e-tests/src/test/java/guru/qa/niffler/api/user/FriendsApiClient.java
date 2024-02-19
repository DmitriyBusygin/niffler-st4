package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class FriendsApiClient extends RestClient {

    private final FriendsApiClient friendsApiClient;

    public FriendsApiClient() {
        super(Config.getInstance().frontUrl());
        friendsApiClient = retrofit.create(FriendsApiClient.class);
    }
}
