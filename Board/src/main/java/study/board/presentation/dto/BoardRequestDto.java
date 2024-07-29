package study.board.presentation.dto;

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
