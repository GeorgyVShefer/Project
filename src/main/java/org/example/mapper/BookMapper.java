package org.example.mapper;

import org.example.dto.*;
import org.example.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    public List<BookGetAllRs> toBookGetAll(List<Book> books) {

        List<BookGetAllRs> listBook = new ArrayList<>();
        for (Book book : books) {
            BookGetAllRs bookGetAllRs = new BookGetAllRs();
            bookGetAllRs.setId(book.getId());
            bookGetAllRs.setTitle(book.getTitle());
            bookGetAllRs.setAuthorId(book.getAuthorId());
            bookGetAllRs.setPublisherId(book.getPublisherId());
            bookGetAllRs.setPublicationYear(book.getPublicationYear());

            listBook.add(bookGetAllRs);
        }
        return listBook;
    }

    public BookGetByIdRs toBookGetByIdRs(Book book) {
        return BookGetByIdRs.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthorId())
                .publisherId(book.getPublisherId())
                .publicationYear(book.getPublicationYear())
                .build();
    }

    public Book toBookSave(BookSaveRq bookSaveRq) {
        return Book.builder()
                .id(bookSaveRq.getId())
                .title(bookSaveRq.getTitle())
                .authorId(bookSaveRq.getAuthorId())
                .publisherId(bookSaveRq.getPublisherId())
                .publicationYear(bookSaveRq.getPublicationYear())
                .build();
    }

    public BookSaveRs toBookSaveRs(BookSaveRq bookSaveRq) {
        return BookSaveRs.builder()
                .id(bookSaveRq.getId())
                .title(bookSaveRq.getTitle())
                .authorId(bookSaveRq.getAuthorId())
                .publisherId(bookSaveRq.getPublisherId())
                .publicationYear(bookSaveRq.getPublicationYear())
                .build();
    }

    public Book toBookUpdate(BookUpdateRq bookUpdateRq){
        return Book.builder()
                .title(bookUpdateRq.getTitle())
                .authorId(bookUpdateRq.getAuthorId())
                .publisherId(bookUpdateRq.getPublisherId())
                .publicationYear(bookUpdateRq.getPublicationYear())
                .build();
    }

    public BookUpdateRs toBookUpdateRs(BookUpdateRq bookUpdateRq){
        return BookUpdateRs.builder()
                .title(bookUpdateRq.getTitle())
                .authorId(bookUpdateRq.getAuthorId())
                .publisherId(bookUpdateRq.getPublisherId())
                .publicationYear(bookUpdateRq.getPublicationYear())
                .build();
    }
}
