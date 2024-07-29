package study.board.presentation.dto;

public record BoardRequestDto(
    String title,
    String userName,
    String password,
    String content
) {

}
