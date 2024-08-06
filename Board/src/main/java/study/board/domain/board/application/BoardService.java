package study.board.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.global.error.exception.GlobalException;
import study.board.domain.board.domain.Board;
import study.board.domain.board.dto.req.BoardDeleteRequestDto;
import study.board.domain.board.dto.req.BoardRequestDto;
import study.board.domain.board.dto.req.BoardSearchCondition;
import study.board.domain.board.dto.res.BoardResponseDto;
import study.board.domain.board.repo.BoardRepository;
import study.board.global.error.ErrorCode;

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
    public Page<BoardResponseDto> readAll(BoardSearchCondition condition, Pageable pageable) {
        return boardRepository.findBoardPage(condition, pageable);
    }

    @Transactional(readOnly = true)
    public BoardResponseDto readOne(Long id) {
        Board board = boardRepository.findByIdAndIsActiveIsTrue(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.BOARD_IS_NOT_EXIST));
        return BoardResponseDto.from(board);
    }

    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findByIdAndIsActiveIsTrue(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.BOARD_IS_NOT_EXIST));
        if (!passwordEncoder.matches(requestDto.password(), board.getPassword())) {
            throw new GlobalException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
        }
        board.update(requestDto.title(), requestDto.content());

        return BoardResponseDto.from(board);
    }

    @Transactional
    public Long delete(Long id, BoardDeleteRequestDto requestDto) {
        Board board = boardRepository.findByIdAndIsActiveIsTrue(id)
            .orElseThrow(() -> new GlobalException(ErrorCode.BOARD_IS_NOT_EXIST));
        if (!passwordEncoder.matches(requestDto.password(), board.getPassword())) {
            throw new GlobalException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
        }
        board.delete();
        return board.getId();
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
