package study.board.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.global.exception.BoardException;
import study.board.global.exception.ErrorCode;
import study.board.presentation.dto.BoardRequestDto;
import study.board.presentation.dto.BoardResponseDto;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper mapper;


    @Transactional
    public Long publishBoard(BoardRequestDto requestDto){
        Board save = boardRepository.save(mapper.map(requestDto,Board.class));
        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> readAll(){
        boardRepository.findByIsActiveIsTrue();
        return null;
    }


}
