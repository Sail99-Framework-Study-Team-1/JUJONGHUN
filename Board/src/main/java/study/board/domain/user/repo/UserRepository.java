package study.board.domain.user.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import study.board.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean findByUserName(String userName);

}
