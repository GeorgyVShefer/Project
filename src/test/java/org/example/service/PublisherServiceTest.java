package org.example.service;

import org.example.dao.PublisherDAO;
import org.example.dto.*;
import org.example.mapper.PublisherMapper;
import org.example.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PublisherServiceTest {
    private PublisherDAO dao;
    private PublisherService service;
    private PublisherMapper mapper;

    @BeforeEach
    public void setup(){
        dao = Mockito.mock(PublisherDAO.class);
        mapper = new PublisherMapper();
        service = new PublisherService(dao,mapper);
    }

    @Test
    public void testShouldReturnAllPublishers(){
        List<Publisher> expected = new ArrayList<>();

        expected.add(new Publisher(1, "pub1", new ArrayList<>()));
        expected.add(new Publisher(2, "pub2", new ArrayList<>()));

        when(dao.getAll()).thenReturn(expected);

        List<PublisherGetAllRs> actual = service.getAll();

        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(1).getId(), actual.get(1).getId());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(1).getName(), actual.get(1).getName());
    }

    @Test
    public void testShouldReturnBookById(){
        Publisher expected = new Publisher(1, "pub1", new ArrayList<>());

        when(dao.getById(1)).thenReturn(expected);

        PublisherGetByIdRs actual = service.getById(1);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getBooks(), actual.getBooks());
    }

    @Test
    public void testShouldSavePublisher(){
        PublisherSaveRq publisherSaveRq = new PublisherSaveRq(1, "pub1", new ArrayList<>());
        Publisher expected = new Publisher(publisherSaveRq.getId(), publisherSaveRq.getName(),publisherSaveRq.getBooks());

        when(dao.save(any())).thenReturn(expected);

        PublisherSaveRs actual = service.save(publisherSaveRq);

        assertEquals(expected.getId(),expected.getId());
        assertEquals(expected.getName(),expected.getName());
        assertEquals(expected.getBooks(),expected.getBooks());
    }

    @Test
    public void testShouldUpdatePublisherById(){
        PublisherUpdateRq publisherUpdateRq = new PublisherUpdateRq("pub1");

        int id = 1;

        Publisher expected = new Publisher(id, publisherUpdateRq.getName(), new ArrayList<>());

        PublisherUpdateRs actual = service.update(id, publisherUpdateRq);

        assertEquals(actual.getName(), actual.getName());
    }

    @Test
    public void testShouldDeletePublisherByIdIsSuccess(){
        when(dao.deleteById(1)).thenReturn(true);

        String result = service.delete(1);

        assertEquals("Издатель с id 1 удален", result);
    }

    @Test
    public void testShouldDeletePublisherByIdIsFail(){
        when(dao.deleteById(1)).thenReturn(false);

        String result = service.delete(1);

        assertEquals("Издателя под таким id нет!", result);
    }
}
