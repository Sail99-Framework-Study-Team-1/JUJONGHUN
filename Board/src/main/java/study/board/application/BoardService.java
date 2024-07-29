package study.board.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.presentation.dto.BoardRequestDto;
import study.board.presentation.dto.BoardResponseDto;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long publishBoard(BoardRequestDto requestDto){
        Board save = boardRepository.save(Board.of(requestDto));
        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> readAll(){
        return boardRepository.findAll().stream()
            .map(BoardResponseDto::from)
            .toList();
    }


}
