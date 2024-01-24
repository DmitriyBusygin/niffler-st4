package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

    private final SelenideElement
            table = $(".table");

    private final ElementsCollection
            peopleRows = table.$$("tbody tr");

    @Step("В таблице есть пользователь {user} с текстом Pending invitation")
    public PeoplePage checkUserWithPendingInvitationInTable(UserJson user) {
        peopleRows.find(text(user.testData().friendsName()))
                .shouldBe(text("Pending invitation"));
        return this;
    }
}
