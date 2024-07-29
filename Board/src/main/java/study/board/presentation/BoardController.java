package study.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.board.global.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BoardController {


    @GetMapping("/board")
    public ResponseEntity<ApiResponse<String>> example(){
        return ApiResponse.createResponse(true,"안녕","resquest successful", HttpStatus.CREATED);
    }



}
