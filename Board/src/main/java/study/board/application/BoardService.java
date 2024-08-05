package study.board.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public BoardResponseDto publishBoard(BoardRequestDto requestDto) {
        Board save = boardRepository.save(createBoard(requestDto));
        return BoardResponseDto.from(save);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> readAll() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(BoardResponseDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponseDto readOne(Long id) {
        Board board = boardRepository.findByIdAndIsActiveIsTrue(id)
            .orElseThrow(() -> new BoardException(ErrorCode.BOARD_IS_NOT_EXIST));
        return BoardResponseDto.from(board);
    }


    private Board createBoard(BoardRequestDto requestDto) {
        return Board.builder()
            .title(requestDto.title())
            .userName(requestDto.userName())
            .password(passwordEncoder.encode(requestDto.password()))
            .content(requestDto.content())
            .build();
    }
}
