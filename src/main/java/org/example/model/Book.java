package org.example.model;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private int book_id;
    private String title;
    private int author_id;
    private int publisher_id;
    private LocalDate publicationYear;
}
