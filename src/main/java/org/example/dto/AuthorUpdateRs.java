package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.model.Book;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthorUpdateRs {
    private String name;
}
