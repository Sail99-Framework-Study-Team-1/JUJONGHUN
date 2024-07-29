package study.board.presentation.dto;

import java.time.LocalDateTime;
import study.board.domain.entity.Board;

public record BoardResponseDto(
    String title,
    String userName,
    String content,
    LocalDateTime createdAt

) {

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
            board.getTitle(),
            board.getUserName(),
            board.getContent(),
            board.getCreatedAt()
        );
    }

}
