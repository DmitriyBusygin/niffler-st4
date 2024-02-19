package guru.qa.niffler.api.user;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FriendsApi {
    @GET("/friends")
    Call<List<UserJson>> friends(@Query("username") String username,
                                 @Query("includePending") boolean includePending);

    @GET("/invitations")
    Call<List<UserJson>> invitations(@Query("username") String username);

    @POST("/acceptInvitation")
    Call<List<UserJson>> acceptInvitation(@Body String username,
                                          @Body FriendJson invitation);

    @POST("/declineInvitation")
    Call<List<UserJson>> declineInvitation(@Body String username,
                                           @Body FriendJson invitation);

    @POST("/addFriend")
    Call<UserJson> addFriend(@Body String username,
                             @Body FriendJson friend);

    @DELETE("/removeFriend")
    Call<List<UserJson>> removeFriend(@Query("username") String username,
                                      @Query("friendUsername") String friendUsername);
}
