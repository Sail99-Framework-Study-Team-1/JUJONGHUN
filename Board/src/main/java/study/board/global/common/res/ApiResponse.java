package study.board.global.common.res;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }


    public static <T> ResponseEntity<ApiResponse<T>> createResponse(boolean success, T data, String message, HttpStatus status) {
        return new ResponseEntity<>(new ApiResponse<>(success, data, message), status);
    }
}
