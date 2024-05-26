package models;

import lombok.Data;

import java.util.List;

@Data
public class BookstoreBooksPostRequestModel {
    String userId;
    List<BookstoreBooksPostIsbnSectionModel> collectionOfIsbns;
}
