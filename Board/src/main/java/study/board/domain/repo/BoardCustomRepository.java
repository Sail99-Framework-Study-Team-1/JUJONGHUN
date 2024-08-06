package study.board.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.board.presentation.dto.BoardResponseDto;
import study.board.presentation.dto.BoardSearchCondition;

public interface BoardCustomRepository {
    Page<BoardResponseDto> findBoardPage(BoardSearchCondition condition, Pageable pageable);


    }
