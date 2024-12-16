package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.dto.*;
import org.example.mapper.BookMapper;
import org.example.model.Book;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookService {
    private BookDAO bookDAO;
    private BookMapper mapper;

    public List<BookGetAllRs> getAll(){
        List<Book> allBooks = bookDAO.getAll();
        return mapper.toBookGetAll(allBooks);
    }

    public BookGetByIdRs getById(int id){
        Book book = bookDAO.getBookById(id);
        return mapper.toBookGetByIdRs(book);
    }

    public BookSaveRs save(BookSaveRq bookSaveRq){
        bookDAO.save(mapper.toBookSave(bookSaveRq));
        return mapper.toBookSaveRs(bookSaveRq);
    }

    public BookUpdateRs update(int id, BookUpdateRq bookUpdateRq){
        bookDAO.update(id, mapper.toBookUpdate(bookUpdateRq));
        return mapper.toBookUpdateRs(bookUpdateRq);
    }

    public String delete(int id){
        if (bookDAO.delete(id)){
            return "Книга с id " + id + " была удалена";
        }
        return "Такого id не существует";
    }
}
