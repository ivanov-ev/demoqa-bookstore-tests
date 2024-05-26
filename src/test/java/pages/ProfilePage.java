package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement deleteInput = $("#delete-record-undefined");
    private final SelenideElement confirmDeletionOkInput = $("#closeSmallModal-ok");
    private final SelenideElement booksTable = $(".ReactTable");

    public ProfilePage openProfilePage() {
        Selenide.open("/profile");
        return this;
    }

    public ProfilePage deleteBook() {
        deleteInput.click();
        confirmDeletionOkInput.click();
        return this;
    }

    public ProfilePage profileDoesNotContainDeletedBook(String book) {
        booksTable.shouldNotHave(text(book));
        return this;
    }
}
