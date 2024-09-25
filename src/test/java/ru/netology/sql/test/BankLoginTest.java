package ru.netology.sql.test;

import org.junit.jupiter.api.*;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.SQLHelper;
import ru.netology.sql.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.sql.data.SQLHelper.cleanAuthCodes;
import static ru.netology.sql.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
//        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://Localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with existing username and password from test data")
    void ShouldSuccessfullyLoginToDashboardWithExistingUsernameNndPasswordFromTestData() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Error message should be displayed if the user is not in the database")
    void ErrorMessageShouldBeDisplayedIfTheUserIsNotInTheDatabase() {
        var authinfo = DataHelper.generateRandomUser();
        loginPage.login(authinfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Error message should be displayed if log in with an existing active user in the database and random verification code")
    void ErrorMessageShouldBeDisplayedIfLogInWithAnExistingActiveUserInTheDatabaseAndRandomVerificationCode () {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}