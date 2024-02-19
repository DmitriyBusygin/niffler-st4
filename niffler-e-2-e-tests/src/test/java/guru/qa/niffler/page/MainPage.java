package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    private static final String HEADER_TEXT = "Niffler. The coin keeper.";

    private final SelenideElement
            header = $("h1[class=header__title]"),
            table = $(".spendings-table"),
            deleteSelectedButton = $(byText("Delete selected")),
            friendsButton = $("a[href='/friends']"),
            allPeopleButton = $("a[href='/people']");

    private final ElementsCollection
            spendRows = table.$$("tbody tr");

    @Step("Ожидать загрузку страницы")
    public MainPage loadPage() {
        header.should(text(HEADER_TEXT));
        return this;
    }

    @Step("Выделить строку в таблице расходов с описанием {description}")
    public MainPage selectSpendByDescription(String description) {
        spendRows.find(text(description))
                .$("td")
                .scrollTo()
                .click();
        return this;
    }

    @Step("Количество строк в таблице расходов равно {count}")
    public MainPage checkCountRowsInSpendingTable(int count) {
        spendRows.shouldHave(size(count));
        return this;
    }

    @Step("Нажать на кнопку Delete selected")
    public MainPage clickDeleteSelected() {
        deleteSelectedButton.click();
        return this;
    }

    @Step("Нажать на кнопку Friends")
    public FriendsPage clickFriends() {
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Нажать на кнопку All people")
    public PeoplePage clickAllPeople() {
        allPeopleButton.click();
        return new PeoplePage();
    }
}
