package study.board.domain.board.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.board.domain.board.dto.res.BoardResponseDto;
import study.board.domain.board.dto.req.BoardSearchCondition;

public interface BoardCustomRepository {
    Page<BoardResponseDto> findBoardPage(BoardSearchCondition condition, Pageable pageable);


    }
