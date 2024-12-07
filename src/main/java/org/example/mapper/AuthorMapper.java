package org.example.mapper;

import org.example.dto.AuthorSaveRq;
import org.example.dto.AuthorSaveRs;
import org.example.model.Author;

public class AuthorMapper {
    public Author toAuthors(AuthorSaveRq authorSaveRq) {
        return Author.builder()
                .id(authorSaveRq.getId())
                .name(authorSaveRq.getName())
                .build();
    }
    public AuthorSaveRs toAuthorSaveRs(AuthorSaveRq authorSaveRq){
        return AuthorSaveRs.builder()
                .id(authorSaveRq.getId())
                .name(authorSaveRq.getName())
                .build();
    }
}
