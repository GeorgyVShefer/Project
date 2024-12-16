package org.example.service;

import org.example.dao.BookDAO;
import org.example.dto.*;
import org.example.mapper.BookMapper;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BookServiceTest {
    private BookDAO bookDAO;
    private BookService service;
    private BookMapper mapper;
    @BeforeEach
    public void setup(){
        bookDAO = Mockito.mock(BookDAO.class);
        mapper = new BookMapper();
        service = new BookService(bookDAO,mapper);
    }

    @Test
    public void testShouldReturnAllAuthors(){
        List<Book> expected = new ArrayList<>();
        Publisher publisher = new Publisher();
        Author author = new Author();
        expected.add(new Book(1, "Tom", author, publisher, new Date(1883-02-22)));

        when(bookDAO.getAll()).thenReturn(expected);

        List<BookGetAllRs> actual = service.getAll();

        assertEquals(expected.get(0).getId(),actual.get(0).getId());
        assertEquals(expected.get(0).getTitle(),actual.get(0).getTitle());
        assertEquals(expected.get(0).getAuthorId(),actual.get(0).getAuthorId());
        assertEquals(expected.get(0).getPublisherId(),actual.get(0).getPublisherId());
        assertEquals(expected.get(0).getPublicationYear(),actual.get(0).getPublicationYear());
    }

    @Test
    public void testShouldReturnBookById(){
        Book expected = new Book(1, "some book", new Author(), new Publisher(), new Date(1993 - 02 - 02));
        when(bookDAO.getBookById(1)).thenReturn(expected);

        BookGetByIdRs actual = service.getById(1);

        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getTitle(),actual.getTitle());
        assertEquals(expected.getAuthorId(),actual.getAuthorId());
        assertEquals(expected.getPublisherId(),actual.getPublisherId());
        assertEquals(expected.getPublicationYear(),actual.getPublicationYear());
    }

    @Test
    public void testShouldSaveBook(){
        BookSaveRq book = new BookSaveRq(1,"some title", new Author(), new Publisher(), new Date());
        Book expected = new Book(book.getId(),book.getTitle(),book.getAuthorId(),book.getPublisherId(),book.getPublicationYear());

        when(bookDAO.save(any())).thenReturn(expected);

        BookSaveRs actual = service.save(book);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthorId(), actual.getAuthorId());
        assertEquals(expected.getPublisherId(), actual.getPublisherId());
        assertEquals(expected.getPublicationYear(), actual.getPublicationYear());
    }

    @Test
    public void testShouldUpdateBook(){
        BookUpdateRq bookUpdateRq = new BookUpdateRq("new Book", new Author(), new Publisher(), new Date());

        int id = 1;

        Book expected = new Book(id, bookUpdateRq.getTitle(), bookUpdateRq.getAuthorId(),
                bookUpdateRq.getPublisherId(), bookUpdateRq.getPublicationYear());

        when(bookDAO.update(id,expected)).thenReturn(expected);

        BookUpdateRs actual = service.update(id, bookUpdateRq);

        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthorId(), actual.getAuthorId());
        assertEquals(expected.getPublisherId(), actual.getPublisherId());
        assertEquals(expected.getPublicationYear(), actual.getPublicationYear());
    }

    @Test
    public void testShouldDeleteBookByIdIsSuccess(){
        when(bookDAO.delete(1)).thenReturn(true);

        String result = service.delete(1);

        assertEquals("Книга с id 1 была удалена", result);

    }

    @Test
    public void testShouldDeleteByIdIsFail(){
        when(bookDAO.delete(1)).thenReturn(false);

        String result = service.delete(1);

        assertEquals("Такого id не существует", result);
    }
}