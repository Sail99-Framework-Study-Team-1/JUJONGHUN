package study.board.unittest.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.board.application.BoardService;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.presentation.dto.BoardRequestDto;
import study.board.presentation.dto.BoardResponseDto;

@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService target;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private BoardRequestDto requestDto;
    private Board board;

    @BeforeEach
    void setUp() {
        requestDto = new BoardRequestDto("Title", "UserName", "password", "Content");
        board = Board.builder()
            .title("Title")
            .userName("UserName")
            .password("encodedPassword")
            .content("Content")
            .build();
    }

    @Test
    void 게시글_쓰기() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when
        BoardResponseDto responseDto = target.publishBoard(requestDto);

        // then
        assertThat(responseDto)
            .isNotNull()
            .extracting(BoardResponseDto::title, BoardResponseDto::userName, BoardResponseDto::content)
            .containsExactly("Title", "UserName", "Content");

        verify(passwordEncoder).encode("password");
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void 게시글_전체조회() {
        // given
        when(boardRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(board));

        // when
        List<BoardResponseDto> responseDtos = target.readAll();

        // then(assert chaining)
        assertThat(responseDtos)
            .isNotEmpty()
            .hasSize(1)
            .first()
            .extracting(BoardResponseDto::title, BoardResponseDto::userName, BoardResponseDto::content)
            .containsExactly("Title", "UserName", "Content");
    }

}
