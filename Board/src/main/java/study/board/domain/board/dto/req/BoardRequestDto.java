package study.board.domain.board.dto.req;


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
