package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.AuthorDAO;
import org.example.dto.*;
import org.example.mapper.AuthorMapper;
import org.example.model.Author;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AuthorService {
    private AuthorDAO authorDAO;
    private AuthorMapper mapper;
    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public List<AuthorGetAllRs> getAll() {
        List<Author> allAuthors = authorDAO.getAll();

        List<AuthorGetAllRs> authors = new ArrayList<>();

        for (Author author : allAuthors) {
            AuthorGetAllRs authorGetAllRs = new AuthorGetAllRs();
            authorGetAllRs.setId(author.getId());
            authorGetAllRs.setName(author.getName());
            authorGetAllRs.setBooks(author.getBooks());
            authors.add(authorGetAllRs);
        }
        return authors;
    }

    public AuthorGetByIdRs getAuthorById(int id) {
        AuthorGetByIdRs authorGetByIdRs = new AuthorGetByIdRs();

        Author author = authorDAO.getAuthorById(id);

        if (author != null) {
            authorGetByIdRs.setId(author.getId());
            authorGetByIdRs.setName(author.getName());
            authorGetByIdRs.setBooks(author.getBooks());
        }

        return authorGetByIdRs;
    }

    public AuthorSaveRs save(AuthorSaveRq authorSaveRq) {
        authorDAO.save(mapper.toAuthors(authorSaveRq));
        return mapper.toAuthorSaveRs(authorSaveRq);
    }

    public AuthorUpdateRs update(int id, AuthorUpdateRq authorUpdateRq){
        authorDAO.update(id, mapper.toAuthors(authorUpdateRq));
        return  mapper.toAuthorUpdateRs(authorUpdateRq);
    }
}
