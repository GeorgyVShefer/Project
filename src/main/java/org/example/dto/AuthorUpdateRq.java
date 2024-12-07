package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorUpdateRq {
    private String name;
}
