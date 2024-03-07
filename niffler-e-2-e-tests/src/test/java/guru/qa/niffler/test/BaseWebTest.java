package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

    final WelcomePage welcomePage = new WelcomePage();
    final LoginPage loginPage = new LoginPage();
    final MainPage mainPage = new MainPage();
    final FriendsPage friendsPage = new FriendsPage();
    final PeoplePage peoplePage = new PeoplePage();
}
