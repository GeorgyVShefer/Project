package org.example.service;


import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.example.dao.AuthorDAO;
import org.example.dto.*;
import org.example.mapper.AuthorMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


class AuthorServiceTest {
    private AuthorService authorService;
    private AuthorDAO authorDAO;
    private AuthorMapper authorMapper;
    @BeforeEach
    public void setup(){
       authorDAO = Mockito.mock(AuthorDAO.class);
        authorMapper = new AuthorMapper();
       authorService = new AuthorService(authorDAO, authorMapper);
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
    public void testGetAuthorById_Should_Return_Author(){
        Author expected = new Author(1,"Maxim Gorky", new ArrayList<Book>());

        when(authorDAO.getAuthorById(1)).thenReturn(expected);

        AuthorGetByIdRs actual = authorService.getAuthorById(1);

        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getName(),actual.getName());
        assertEquals(expected.getBooks(),actual.getBooks());
    }
    @Test
    @DisplayName("Добавляет нового автора в таблицу авторов")
    public void testSaveAuthor_ShouldAddAuthor_WhenQueryIsSuccess(){
        AuthorSaveRq author = new AuthorSaveRq(1, "Max Montana", new ArrayList<Book>());
        Author expected = new Author(author.getId(), author.getName(), author.getBooks());
        when(authorDAO.save(any())).thenReturn(expected);

        AuthorSaveRs actual = authorService.save(author);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void testUpdate_ShouldReturnUpdateAuthor(){
        AuthorUpdateRq authorUpdateRq = new AuthorUpdateRq("Alexandr");
        int id = 1;
        Author expected = new Author(id, authorUpdateRq.getName(), new ArrayList<Book>());
        when(authorDAO.update(id,expected)).thenReturn(expected);

        AuthorUpdateRs actual = authorService.update(id, authorUpdateRq);

        assertEquals(expected.getName(), actual.getName());
    }



    @Test
    void testDeleteByIdIsSuccess() {
        when(authorDAO.deleteAuthorById(1)).thenReturn(true);

        String result = authorService.deleteById(1);

        assertEquals("Автор удален под id 1", result);
    }

    @Test
    void testDeleteByIdIsFail() {
        when(authorDAO.deleteAuthorById(2)).thenReturn(false);

        String result = authorService.deleteById(2);

        assertEquals("Такого id не существует", result);
    }
}