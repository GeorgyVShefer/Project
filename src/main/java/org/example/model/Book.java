package org.example.model;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private int id;
    private String title;
    private Author authorId;
    private Publisher publisherId;
    private LocalDate publicationYear;
}
