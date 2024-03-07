package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement
            firstNameField = $x("//input[@name='firstname']"),
            surnameField = $x("//input[@name='surname']"),
            categoryField = $x("//input[@name='category']"),
            createBtn = $x("//button[text()='Create']"),
            submitBtn = $x("//button[text()='Submit']");

    @Step("Нажать кнопку Create")
    public ProfilePage clickCreateBtn() {
        createBtn.click();
        return this;
    }

    @Step("Нажать кнопку Submit")
    public ProfilePage clickSubmitBtn() {
        submitBtn.click();
        return this;
    }

    @Step("Заполнить поле Firstname значением {firstname}")
    public ProfilePage setFirstname(String firstname) {
        firstNameField.setValue(firstname);
        return this;
    }

    @Step("Заполнить поле Surname значением {surname}")
    public ProfilePage setSurname(String surname) {
        surnameField.setValue(surname);
        return this;
    }

    @Step("Заполнить поле Category значением {category}")
    public ProfilePage setCategory(String category) {
        categoryField.setValue(category);
        return this;
    }
}