package study.board.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long publishBoard(BoardRequestDto requestDto){
        Board save = boardRepository.save(createBoard(requestDto));
        return save.getId();
    }

    private Board createBoard(BoardRequestDto requestDto) {
        return Board.builder()
           .title(requestDto.title())
           .userName(requestDto.userName())
           .password(passwordEncoder.encode(requestDto.password()))
           .content(requestDto.content())
           .build();
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> readAll(){
        return boardRepository.findAll().stream()
            .map(BoardResponseDto::from)
            .toList();
    }


}
