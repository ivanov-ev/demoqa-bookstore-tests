package tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import models.*;
import org.junit.jupiter.api.*;
import pages.*;
import org.openqa.selenium.Cookie;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.AccountLoginSpec.AccountLoginRequestSpec;
import static specs.AccountLoginSpec.AccountLoginResponseSpec;
import static specs.BookstoreBooksDeleteSpec.BookstoreBooksDeleteRequestSpec;
import static specs.BookstoreBooksDeleteSpec.BookstoreBooksDeleteResponseSpec;
import static specs.BookstoreBooksPostSpec.BookstoreBooksPostRequestSpec;
import static specs.BookstoreBooksPostSpec.BookstoreBooksPostResponseSpec;


@Tag("all_tests")
@Feature("demoqa")
@Story("books")
@Owner("ivanov-ev")
@Severity(SeverityLevel.NORMAL)
public class BooksInProfileTests extends TestBase{

    public String accountLoginUserID, accountLoginToken;
    public String isbn = "9781449365035";

    @BeforeAll
    public static void configureRestAssured() {
        RestAssured.baseURI = "https://demoqa.com";
        RestAssured.basePath = "/";
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @DisplayName("Delete a book from a user profile")
    void DeleteBookFromProfileTest() {


          step("Preparatory step: Log in and set authentication cookies", () -> {

              AccountLoginRequestModel jsonRequestBody = new AccountLoginRequestModel();
              jsonRequestBody.setUserName("JooohnDoooe");
              jsonRequestBody.setPassword("nbdW2h!#$");

              AccountLoginResponseModel accountLoginResponse = step("Perform a POST request", ()->
                      given(AccountLoginRequestSpec)
                              .body(jsonRequestBody)

                      .when()
                              .post()

                      .then()
                              .spec(AccountLoginResponseSpec)
                              .extract().as(AccountLoginResponseModel.class)
              );

              Selenide.open("/favicon.png");
              getWebDriver().manage().addCookie(new Cookie("userID", accountLoginResponse.getUserId()));
              getWebDriver().manage().addCookie(new Cookie("expires", accountLoginResponse.getExpires()));
              getWebDriver().manage().addCookie(new Cookie("token", accountLoginResponse.getToken()));

              accountLoginUserID = accountLoginResponse.getUserId();
              accountLoginToken = accountLoginResponse.getToken();

          });




          step("Preparatory step: Delete all books from the user profile", () -> {

              step("Perform a DELETE request", ()->
              given(BookstoreBooksDeleteRequestSpec)
                      .header("Authorization", "Bearer " + accountLoginToken)
                      .queryParam("UserId", accountLoginUserID)

              .when()
                      .delete()

              .then()
                      .spec(BookstoreBooksDeleteResponseSpec)
              );

          });



          step("Preparatory step: Add a specific book to the user profile", () -> {

              BookstoreBooksPostIsbnSectionModel bookstoreBooksPostIsbnSectionModel = new BookstoreBooksPostIsbnSectionModel();
              bookstoreBooksPostIsbnSectionModel.setIsbn(isbn);

              List<BookstoreBooksPostIsbnSectionModel> isbnList = new ArrayList<>();
              isbnList.add(bookstoreBooksPostIsbnSectionModel);

              BookstoreBooksPostRequestModel bookstoreBooksPostRequestModel = new BookstoreBooksPostRequestModel();
              bookstoreBooksPostRequestModel.setUserId(accountLoginUserID);
              bookstoreBooksPostRequestModel.setCollectionOfIsbns(isbnList);


              BookstoreBooksPostResponseModel bookstoreBooksPostResponse = step("Perform a POST request", ()->
              given(BookstoreBooksPostRequestSpec)
                      .header("Authorization", "Bearer " + accountLoginToken)
                      .body(bookstoreBooksPostRequestModel)

              .when()
                      .post()

              .then()
                      .spec(BookstoreBooksPostResponseSpec)
                      .extract().as(BookstoreBooksPostResponseModel.class)
              );

          });



          step("Main step: Delete the book from the user profile via the UI. " +
                  "Check that the book is successfully deleted", () -> {
              ProfilePage profilePage = new ProfilePage();
              profilePage.openProfilePage()
                      .deleteBook()
                      .profileDoesNotContainDeletedBook(isbn);
          });

    }
}
