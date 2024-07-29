package study.board.global.exception;

import lombok.Getter;

@Getter
public enum BoardErrorCode {

    BAD_REQUEST("요청에 오류가 있습니다.", LogType.ERROR, 400, "E001"),
    NOT_FOUND("해당 요청을 찾을 수 없습니다.", LogType.ERROR, 404, "E002");


    private final String message;
    private final LogType logType;
    private final int status;
    private final String code;

    BoardErrorCode(String message, LogType logType, int status, String code) {
        this.message = message;
        this.logType = logType;
        this.status = status;
        this.code = code;
    }

    public enum LogType {
        WARN,
        ERROR
    }
}
