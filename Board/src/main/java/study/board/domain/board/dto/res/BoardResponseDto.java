package study.board.domain.board.dto.res;

import java.time.LocalDateTime;
import study.board.domain.board.domain.Board;

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
