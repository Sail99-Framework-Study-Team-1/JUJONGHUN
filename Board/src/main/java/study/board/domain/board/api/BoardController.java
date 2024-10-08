package study.board.domain.board.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.board.domain.board.dto.req.BoardDeleteRequestDto;
import study.board.domain.board.dto.req.BoardRequestDto;
import study.board.domain.board.dto.res.BoardResponseDto;
import study.board.domain.board.dto.req.BoardSearchCondition;
import study.board.domain.board.application.BoardService;
import study.board.domain.board.dto.res.BoardDeleteResponseDto;
import study.board.global.common.res.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    @PostMapping("")
    public ResponseEntity<ApiResponse<BoardResponseDto>> publishBoard(
        @RequestBody @Valid BoardRequestDto requestDto) {
        return ApiResponse.createResponse(true, boardService.publishBoard(requestDto),
            "게시글 등록 완료", HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<BoardResponseDto>>> readAll(
        @ModelAttribute BoardSearchCondition condition,
        @PageableDefault(size = 5) Pageable pageable
    ) {
        return ApiResponse.createResponse(true, boardService.readAll(condition,pageable),
            "게시글 전체 조회 성공", HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> readOne(@PathVariable Long boardId) {
        return ApiResponse.createResponse(true, boardService.readOne(boardId),
            "게시글 조회 성공", HttpStatus.OK);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> update(
        @PathVariable Long boardId, @RequestBody @Valid BoardRequestDto requestDto) {
        return ApiResponse.createResponse(true, boardService.update(boardId, requestDto),
            "게시글 수정 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDeleteResponseDto>> delete(@PathVariable Long boardId, @RequestBody
    BoardDeleteRequestDto requestDto) {
        return ApiResponse.createResponse(true, boardService.delete(boardId, requestDto),
            "게시글 삭제 성공", HttpStatus.OK);
    }

}
