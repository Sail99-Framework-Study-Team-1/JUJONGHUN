package study.board.unittest.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.unittest.TestConfig;

@DataJpaTest
@Import(TestConfig.class)
@DisplayName("게시판 레포지토리 계층 단위테스트")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("레포지토리 널체크")
    void board_repository_null_check(){
        assertThat(boardRepository).isNotNull();
    }

    @Test
    @DisplayName("게시글 등록")
    void publish_board(){
        // given
        final Board board = Board.builder()
            .title("Test Title")
            .password("1234")
            .content("Test content")
            .userName("jjh")
            .build();

        // when
        final Board result = boardRepository.save(board);

        // then
        assertThat(result)
            .isNotNull()
            .extracting(Board::getTitle, Board::getPassword, Board::getContent, Board::getUserName)
            .containsExactly("Test Title", "1234", "Test content", "jjh");

    }

    @Test
    @DisplayName("게시글 전체 조회")
    void read_all_boards() {
        // given
        final Board board = Board.builder()
            .title("Test Title")
            .password("1234")
            .content("Test content")
            .userName("jjh")
            .build();
        boardRepository.save(board);

        // when
        final List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();

        // then
        assertThat(boards).hasSize(1);
        Board firstBoard = boards.get(0); // .iterator().next() 대신 .get(0) 사용
        assertThat(firstBoard)
            .extracting(Board::getTitle, Board::getPassword, Board::getContent, Board::getUserName)
            .containsExactly("Test Title", "1234", "Test content", "jjh");
    }

}
