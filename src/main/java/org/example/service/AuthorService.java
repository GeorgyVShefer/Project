package org.example.service;

import org.example.dao.AuthorDAO;
import org.example.dto.AuthorGetAllRs;
import org.example.dto.AuthorGetByIdRs;
import org.example.model.Author;
import org.example.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {
   private AuthorDAO authorDAO;

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public List<AuthorGetAllRs> getAll(){
        List<Author> allAuthors = authorDAO.getAll();

        List<AuthorGetAllRs> authors = new ArrayList<>();

        for (Author author : allAuthors){
            AuthorGetAllRs authorGetAllRs = new AuthorGetAllRs();
            authorGetAllRs.setId(author.getId());
            authorGetAllRs.setName(author.getName());
            authorGetAllRs.setBooks(author.getBooks());
            authors.add(authorGetAllRs);
        }
        return authors;
    }

    public AuthorGetByIdRs getAuthorById(int id){
        AuthorGetByIdRs authorGetByIdRs = new AuthorGetByIdRs();

        Author author = authorDAO.getAuthorById(id);

        if (author != null) {
            authorGetByIdRs.setId(author.getId());
            authorGetByIdRs.setName(author.getName());
            authorGetByIdRs.setBooks(author.getBooks());
        }

        return authorGetByIdRs;
    }
}
