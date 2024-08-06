package study.board.unittest.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.board.domain.board.application.BoardService;
import study.board.domain.board.domain.Board;
import study.board.domain.board.repo.BoardRepository;
import study.board.global.error.exception.GlobalException;
import study.board.domain.board.dto.req.BoardDeleteRequestDto;
import study.board.domain.board.dto.req.BoardRequestDto;
import study.board.domain.board.dto.res.BoardResponseDto;
import study.board.domain.board.dto.req.BoardSearchCondition;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시판 서비스 계층 단위테스트")
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

        board = spy(board);

    }

    @Test
    @DisplayName("게시글 등록")
    void publish_board() {
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
    @DisplayName("게시글 전체 조회")
    void read_all_boards() {
        // given
        board = Board.builder()
            .title("Title")
            .password("1234")
            .content("Content")
            .userName("UserName")
            .build();

        BoardResponseDto boardResponseDto = new BoardResponseDto(
            board.getTitle(),
            board.getUserName(),
            board.getContent(),
            board.getCreatedAt()
        );

        Pageable pageable = PageRequest.of(0, 5); // 페이지와 페이지 크기 설정
        BoardSearchCondition condition = new BoardSearchCondition(null,null,null,null); // 기본 검색 조건 설정

        Page<Board> boardPage = new PageImpl<>(List.of(board), pageable, 1); // 총 게시글 수를 1로 설정

        // 레포지토리 mocking해서 불러옴
        when(boardRepository.findBoardPage(any(BoardSearchCondition.class), any(Pageable.class)))
            .thenReturn(boardPage.map(b -> new BoardResponseDto(
                b.getTitle(),
                b.getUserName(),
                b.getContent(),
                b.getCreatedAt()
            )));

        // when
        Page<BoardResponseDto> responseDtos = target.readAll(condition, pageable);

        // then
        assertThat(responseDtos).isNotEmpty();
        assertThat(responseDtos.getTotalElements()).isEqualTo(1);
        assertThat(responseDtos.getContent())
            .hasSize(1)
            .first()
            .extracting(BoardResponseDto::title, BoardResponseDto::userName, BoardResponseDto::content)
            .containsExactly(boardResponseDto.title(), boardResponseDto.userName(), boardResponseDto.content());
    }


    @Test
    @DisplayName("게시글 단건 조회")
    void readOne() {
        // given
        when(boardRepository.findByIdAndIsActiveIsTrue(anyLong()))
            .thenReturn(Optional.of(board));

        // when
        BoardResponseDto result = target.readOne(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Title");
    }

    @Test
    @DisplayName("게시글 수정")
    void update() {
        // given
        when(boardRepository.findByIdAndIsActiveIsTrue(anyLong()))
            .thenReturn(Optional.of(board));
        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(true);

        // when
        BoardResponseDto result = target.update(1L, requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Title");
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        // given
        BoardDeleteRequestDto deleteRequestDto = new BoardDeleteRequestDto("1234");
        when(boardRepository.findByIdAndIsActiveIsTrue(anyLong()))
            .thenReturn(Optional.of(board));
        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(true);
        doReturn(1L).when(board).getId();

        // when
        Long result = target.delete(1L, deleteRequestDto);

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 수정 비밀번호 불일치")
    void updatePasswordMismatch() {
        // given
        when(boardRepository.findByIdAndIsActiveIsTrue(anyLong()))
            .thenReturn(Optional.of(board));
        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(false);

        // when & then
        assertThrows(GlobalException.class, () -> target.update(1L, requestDto));
    }

    @Test
    @DisplayName("게시글 삭제 비밀번호 불일치")
    void deletePasswordMismatch() {
        // given
        BoardDeleteRequestDto deleteRequestDto = new BoardDeleteRequestDto("wrong_password");
        when(boardRepository.findByIdAndIsActiveIsTrue(anyLong()))
            .thenReturn(Optional.of(board));
        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(false);

        // when & then
        assertThrows(GlobalException.class, () -> target.delete(1L, deleteRequestDto));
    }
}