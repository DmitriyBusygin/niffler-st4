package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class MyFriendsWithoutBeforeEachTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final FriendsPage friendsPage = new FriendsPage();
    private final PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin() {
        welcomePage.openPage()
                .clickLogin();
    }

    @Test
    void have_friendsWithAnnotation(@User(WITH_FRIENDS) UserJson user) {
        loginPage.loadPage()
                .setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSignIn();

        mainPage.clickFriends();
        friendsPage.checkFriendWithNameInTable(user);
    }

    @Test
    void friendRequestSend(@User(INVITATION_SEND) UserJson user) {
        loginPage.loadPage()
                .setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSignIn();

        mainPage.clickAllPeople();
        peoplePage.checkUserWithPendingInvitationInTable(user);
    }

    @Test
    void IncomingFriendRequest(@User(INVITATION_RECEIVED) UserJson user) {
        loginPage.loadPage()
                .setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSignIn();

        mainPage.clickFriends();

        friendsPage.invitationToBeFriendsFromUser(user);
    }

    @Test
    void twoAnnotationInTest(@User(INVITATION_RECEIVED) UserJson user,
                             @User(INVITATION_SEND) UserJson userAnother) {
        loginPage.loadPage()
                .setUserName(user.username())
                .setPassword(user.testData().password())
                .clickSignIn();

        mainPage.clickFriends();

        friendsPage.invitationToBeFriendsFromUser(user);
    }
}
