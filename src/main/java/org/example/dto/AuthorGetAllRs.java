package org.example.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Book;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorGetAllRs {
    private int id;
    private String name;
    private List<Book> books;

    public AuthorGetAllRs(int i, String author1) {
        this.id = i;
        this.name = author1;
    }

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
