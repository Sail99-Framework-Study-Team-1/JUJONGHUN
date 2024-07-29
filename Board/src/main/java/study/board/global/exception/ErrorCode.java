package study.board.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST("요청에 오류가 있습니다.", LogType.ERROR, 400, "E001"),
    NOT_FOUND("해당 요청을 찾을 수 없습니다.", LogType.ERROR, 404, "E002"),

    // 유저 공통 에러코드 (예상?)
    USER_IS_NOT_EXIST("사용자가 존재 하지 않습니다.", LogType.ERROR, 500, "E100"),
    PASSWORDS_DO_NOT_MATCH("비밀번호가 일치 하지 않습니다.", LogType.ERROR, 500, "E101"),
    USER_IS_ALREADY_EXIST("사용자가 이미 존재합니다.", LogType.ERROR, 500, "E102");

    private final String message;
    private final LogType logType;
    private final int status;
    private final String code;

    ErrorCode(String message, LogType logType, int status, String code) {
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
