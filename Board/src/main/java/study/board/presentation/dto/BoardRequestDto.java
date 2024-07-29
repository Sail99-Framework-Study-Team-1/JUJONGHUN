package study.board.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import study.board.domain.entity.Board;

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
    public static Board of(BoardRequestDto requestDto){
        return Board.builder()
            .title(requestDto.title())
            .userName(requestDto.userName())
            .password(requestDto.password())
            .content(requestDto.content())
            .build();
    }
}
