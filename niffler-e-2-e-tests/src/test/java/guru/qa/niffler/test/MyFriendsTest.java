package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class MyFriendsTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final FriendsPage friendsPage = new FriendsPage();

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
