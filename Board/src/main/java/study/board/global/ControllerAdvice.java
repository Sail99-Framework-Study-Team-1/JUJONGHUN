package study.board.global;

import static java.text.NumberFormat.Field.SUFFIX;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.board.global.exception.ErrorCode;
import study.board.global.exception.BoardException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler({BoardException.class})
    protected ResponseEntity<ErrorResponse<Void>> handleServerException(BoardException e) {
        errorLog("Server Exception occurred", e);
        return ResponseEntity.status(e.getErrorCode().getStatus())
            .body(ErrorResponse.failure(e.getErrorCode()));
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse<Void>> handleValidationException(
        MethodArgumentNotValidException e) {
        errorLog("Method Argument Validation failed", e);
        BindingResult result = e.getBindingResult();
        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < result.getFieldErrors().size(); i++) {
            FieldError error = result.getFieldErrors().get(i);
            errors.append(error.getField()).append(" : ");
            errors.append(error.getDefaultMessage());
            if (i < result.getFieldErrors().size() - 1) {
                errors.append(", ");
            } else {
                errors.append(".");
            }
        }
        return ResponseEntity.status(e.getStatusCode().value())
            .body(ErrorResponse.failure(errors.toString(), ErrorCode.BAD_REQUEST));
    }

    private void errorLog(String errorMessage, Throwable throwable) {
        if (throwable instanceof BoardException serverException) {
            ErrorCode errorCode = serverException.getErrorCode();
            if (errorCode.getLogType() == ErrorCode.LogType.WARN) {
                log.warn("{}" + SUFFIX, errorMessage, errorCode.getMessage());
            } else if (errorCode.getLogType() == ErrorCode.LogType.ERROR) {
                log.error("{}" + SUFFIX, errorMessage, errorCode.getMessage());
            }
        } else {
            log.error(errorMessage, throwable);
        }
    }



}
