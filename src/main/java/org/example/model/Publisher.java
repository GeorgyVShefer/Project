package org.example.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Publisher {
    private int id;
    private String name;
    private List<Book> books;

    @Override
    public String toString() {
        StringBuilder bookTitles = new StringBuilder();
        for (Book book : books) {
            if (bookTitles.length() > 0) bookTitles.append(", ");
            bookTitles.append(book.getTitle());
        }
        return "Publisher{id=" + id + ", name='" + name + "', books=[" + bookTitles + "]}";
    }
}
