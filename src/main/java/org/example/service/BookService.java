package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.dto.BookGetAllRs;
import org.example.model.Book;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookService {
    private BookDAO bookDAO;

    public List<BookGetAllRs> getAll(){
        List<BookGetAllRs> listBookGetAllRs = new ArrayList<>();
        List<Book> books = bookDAO.getAll();
        for (Book book : books){
            BookGetAllRs respBook = new BookGetAllRs();
            respBook.setId(book.getId());
            respBook.setTitle(book.getTitle());
            respBook.setAuthorId(book.getAuthorId());
            respBook.setPublisherId(book.getPublisherId());
            respBook.setPublicationYear(book.getPublicationYear());
            listBookGetAllRs.add(respBook);
        }
        return listBookGetAllRs;
    }
}
