package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class MyFriendsTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(WITH_FRIENDS) UserJson user) {
        welcomePage.openPage()
                .clickLogin();

        loginPage.loadPage()
                .setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSignIn();
    }

    @Test
    void have_friendsWithAnnotation(@User(WITH_FRIENDS) UserJson user) {
        mainPage.clickFriends();
        friendsPage.checkFriendWithNameInTable(user);
    }

    @Test
    void have_friendsWithoutAnnotation() {
        mainPage.clickFriends();
        friendsPage.checkCountRowsInTable(1);
    }
}
