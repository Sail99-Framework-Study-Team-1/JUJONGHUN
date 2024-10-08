package study.board.domain.user.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.board.domain.user.application.UserService;
import study.board.domain.user.dto.req.UserLoginRequestDto;
import study.board.domain.user.dto.req.UserRegisterRequestDto;
import study.board.domain.user.dto.res.UserRegisterResponseDto;
import study.board.global.common.res.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserRegisterResponseDto>> register(
        @Valid @RequestBody UserRegisterRequestDto requestDto) {

        return ApiResponse.createResponse(
            true,
            userService.register(requestDto),
            "회원가입에 성공했습니다.",
            HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
        @Valid @RequestBody UserLoginRequestDto requestDto) {
        return ApiResponse.createResponse(
            true,
            userService.login(requestDto),
            "로그인 성공. atk발급, rtk저장",
            HttpStatus.OK
        );
    }

}
