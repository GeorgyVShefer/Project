package org.example.service;


import org.example.dao.AuthorDAO;
import org.example.dto.AuthorGetAllRs;
import org.example.dto.AuthorGetByIdRs;
import org.example.model.Author;
import org.example.model.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

class AuthorServiceTest {
    private AuthorService authorService;
    private AuthorDAO authorDAO;

    @BeforeEach
    public void setup(){
       authorDAO = Mockito.mock(AuthorDAO.class);
       authorService = new AuthorService(authorDAO);
    }

    @Test
    @DisplayName("Достает всех авторов из БД")
    public void testGetAllAuthors(){
        List<Author> expected = new ArrayList<>();
        expected.add(new Author(4,"Alexandr", new ArrayList<Book>()));
        expected.add(new Author(5,"Alexa", new ArrayList<Book>()));
        expected.add(new Author(6,"xandr", new ArrayList<Book>()));

        when(authorDAO.getAll()).thenReturn(expected);
        List<AuthorGetAllRs> actual = authorService.getAll();
        assertEquals(expected.get(1).getId(),actual.get(1).getId());
        assertEquals(expected.size(),actual.size());
        assertEquals(expected.get(2).getName(),actual.get(2).getName());
    }

    @Test
    @DisplayName("Достает автора из БД по ключу")
    public void testGetAuthorById(){
        Author expected = new Author(1,"Maxim Gorky", new ArrayList<Book>());

        when(authorDAO.getAuthorById(1)).thenReturn(expected);

        AuthorGetByIdRs actual = authorService.getAuthorById(1);

        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getName(),actual.getName());
        assertEquals(expected.getBooks(),actual.getBooks());
    }
}