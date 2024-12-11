package org.example.service;

import org.example.dao.BookDAO;
import org.example.dto.BookGetAllRs;
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
import static org.mockito.Mockito.when;

class BookServiceTest {
    private BookDAO bookDAO;
    private BookService service;

    @BeforeEach
    public void setup(){
        bookDAO = Mockito.mock(BookDAO.class);
        service = new BookService(bookDAO);
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
}