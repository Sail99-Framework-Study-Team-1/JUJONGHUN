package study.board.unittest.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void BoardRepository널체크(){
        assertThat(boardRepository).isNotNull();
    }

    @Test
    public void 게시글등록(){
        //given
        final Board board = Board.builder()
            .title("Test Title")
            .password("1234")
            .content("Test content")
            .userName("jjh")
            .build();

        //when
        final Board result = boardRepository.save(board);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getContent()).isEqualTo("Test content");
        assertThat(result.getUserName()).isEqualTo("jjh");

    }

    @Test
    public void 게시글전체조회(){
        //given
        final Board board = Board.builder()
            .title("Test Title")
            .password("1234")
            .content("Test content")
            .userName("jjh")
            .build();
        boardRepository.save(board);

        //when
        final List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();

        //then
        assertThat(boards).hasSize(1);
        assertThat(boards.iterator().next().getTitle()).isEqualTo("Test Title");
        assertThat(boards.iterator().next().getPassword()).isEqualTo("1234");
        assertThat(boards.iterator().next().getContent()).isEqualTo("Test content");
        assertThat(boards.iterator().next().getUserName()).isEqualTo("jjh");
    }
}
