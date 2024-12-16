package org.example.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
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
        return "Author{id=" + id + ", name='" + name + "', books=[" + bookTitles + "]}";
    }
}
