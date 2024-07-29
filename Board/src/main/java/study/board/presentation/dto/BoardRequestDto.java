package study.board.presentation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record BoardRequestDto(
    @NotBlank
    String title,

    @NotBlank
    String userName,

    @NotBlank
    String password,

    @NotBlank
    String content

) {

}
