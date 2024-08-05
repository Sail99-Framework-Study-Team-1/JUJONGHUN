package study.board.domain.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.board.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByCreatedAtDesc();

    Optional<Board> findByIdAndIsActiveIsTrue(Long id);
}
