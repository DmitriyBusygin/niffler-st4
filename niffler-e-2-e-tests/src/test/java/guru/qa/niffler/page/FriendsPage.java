package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement
            table = $(".table");

    private final ElementsCollection
            friendsRows = table.$$("tbody tr");

    @Step("В таблице есть друг с именем {name}")
    public FriendsPage checkFriendWithNameInTable(UserJson user) {
        friendsRows.find(text(user.testData().friendsName()))
                .shouldBe(visible);
        return this;
    }

    @Step("В таблице есть приглашение дружить от пользователя {user}")
    public FriendsPage invitationToBeFriendsFromUser(UserJson user) {
        friendsRows.find(text(user.testData().friendsName()))
                .$("div[data-tooltip-id='submit-invitation']")
                .shouldBe(visible);
        return this;
    }

    @Step("В таблице друзей количество строк равно {count}")
    public FriendsPage checkCountRowsInTable(int count) {
        friendsRows.should(CollectionCondition.size(count));
        return this;
    }
}
