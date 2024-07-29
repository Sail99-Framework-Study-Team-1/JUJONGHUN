package study.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.repo.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void publishBoard(){
//        boardRepository.save()
    }

    public List<BoardResponseDto> readAll(){

        return null;
    }


}
