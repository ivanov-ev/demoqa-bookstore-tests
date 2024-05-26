package models;

import lombok.Data;

import java.util.List;

@Data
public class BookstoreBooksPostResponseModel {
    List<BookstoreBooksPostIsbnSectionModel> books;
}
