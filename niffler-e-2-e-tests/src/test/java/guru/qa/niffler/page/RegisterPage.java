package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {
    private static final String HEADER_TEXT = "Welcome to Niffler. The coin keeper";

    private final SelenideElement
            header = $("h1[class=form__header]"),
            nameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            repeatPasswordInput = $("input[name='passwordSubmit']"),
            signUpButton = $("button[type='submit']");

    @Step("Ожидать загрузку страницы")
    public RegisterPage loadPage() {
        header.should(text(HEADER_TEXT));
        return this;
    }

    @Step("Заполнить поле UserName {userName}")
    public RegisterPage setUserName(String userName) {
        nameInput.setValue(userName);
        return this;
    }

    @Step("Заполнить поле Password {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Заполнить поле Repeat password {password}")
    public RegisterPage setRepeatPassword(String repeatPassword) {
        repeatPasswordInput.setValue(repeatPassword);
        return this;
    }

    @Step("Нажать на кнопку Sign Up")
    public RegisterPage clickSignUp() {
        signUpButton.click();
        return this;
    }
}
