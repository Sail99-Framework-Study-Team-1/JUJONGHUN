package study.board.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.board.application.BoardService;
import study.board.global.ApiResponse;
import study.board.presentation.dto.BoardRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/board")
    public ResponseEntity<ApiResponse<Long>> publishBoard(
        @RequestBody @Valid BoardRequestDto requestDto){
        return ApiResponse.createResponse(true, boardService.publishBoard(requestDto),
            "게시글 등록 완료", HttpStatus.CREATED);
    }



}
