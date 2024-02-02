package guru.qa.niffler.test;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.jupiter.DbUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.Test;

public class MyLoginTest extends BaseWebTest {

    private final WelcomePage welcomePage = new WelcomePage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @DbUser(userName = "lala", password = "12345")
    @Test
    void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
        welcomePage.openPage()
                .clickLogin();

        loginPage.loadPage()
                .setUserName(userAuth.getUsername())
                .setPassword(userAuth.getPassword())
                .clickSignIn();

        mainPage.loadPage();
    }
}
