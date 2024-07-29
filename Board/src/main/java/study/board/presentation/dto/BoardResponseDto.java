package study.board.presentation.dto;

import java.time.LocalDateTime;
import study.board.domain.entity.Board;

public record BoardResponseDto(
    Long id,
    String title,
    String userName,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
    public static BoardResponseDto from(Board board){
        return new BoardResponseDto(
            board.getId(),
            board.getTitle(),
            board.getUserName(),
            board.getContent(),
            board.getCreatedAt(),
            board.getModifiedAt()
        );
    }

}
