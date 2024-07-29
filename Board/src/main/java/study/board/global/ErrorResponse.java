package study.board.global;

import java.time.LocalDateTime;
import lombok.Getter;
import study.board.global.exception.ErrorCode;

@Getter
public class ErrorResponse<T> {
    private LocalDateTime timeStamp;

    private String message;

    private String code;

    public ErrorResponse(LocalDateTime timeStamp, String message, String code) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.code = code;
    }
    public static <T> ErrorResponse<T> failure(ErrorCode errorCode) {
        return new ErrorResponse<>(LocalDateTime.now(),errorCode.getMessage(), errorCode.getCode());
    }

    public static <T> ErrorResponse<T> failure(String message, ErrorCode errorCode) {
        return new ErrorResponse<>(LocalDateTime.now(), message, errorCode.getCode());
    }

}
