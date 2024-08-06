package study.board.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.board.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {
    private final ErrorCode errorCode;

}
