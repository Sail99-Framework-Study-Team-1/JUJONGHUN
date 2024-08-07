package study.board.domain.board.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import study.board.domain.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> ,BoardCustomRepository{

    List<Board> findAllByOrderByCreatedAtDesc();
}
