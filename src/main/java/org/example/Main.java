package org.example;

import org.example.dao.AuthorDAO;
import org.example.service.AuthorService;
import org.example.util.ConnectionUtil;

public class Main {
    public static void main(String[] args) {
        AuthorDAO authorDAO = new AuthorDAO(new ConnectionUtil());

        AuthorService authorService = new AuthorService(authorDAO);
        System.out.println(authorService.getAll());

    }
}
