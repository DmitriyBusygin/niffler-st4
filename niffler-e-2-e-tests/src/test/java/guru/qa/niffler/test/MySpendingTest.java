package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.Test;

public class MySpendingTest extends BaseWebTest {
    static {
        Configuration.browserSize = "1980x1024";
    }

    @GenerateCategory(
            username = "duck",
            category = "Обучение")
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            category = "Обучение",
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.openPage()
                .clickLogin();

        loginPage.loadPage()
                .setUserName(spend.username())
                .setPassword("12345")
                .clickSignIn();

        mainPage.loadPage()
                .selectSpendByDescription(spend.description())
                .clickDeleteSelected()
                .checkCountRowsInSpendingTable(0);
    }
}
