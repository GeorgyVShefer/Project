package org.example.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Publisher {
    private int id;
    private String name;
    private List<Book> books;
}
