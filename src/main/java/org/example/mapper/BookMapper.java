package org.example.mapper;

import org.example.dto.BookGetByIdRs;
import org.example.model.Book;

public class BookMapper {

    public BookGetByIdRs toBookGetByIdRs(Book book){
        return BookGetByIdRs.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorId(book.getAuthorId())
                .publisherId(book.getPublisherId())
                .publicationYear(book.getPublicationYear())
                .build();
    }
}
