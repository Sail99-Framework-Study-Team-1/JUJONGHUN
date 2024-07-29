package study.board.global.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import study.board.global.ErrorResponse;
import study.board.global.exception.ErrorCode;

@NoArgsConstructor
public class ErrorMessageUtils {

    public static void makeErrorResponseBody(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        mapper.writeValue(response.getWriter(), ErrorResponse.failure(errorCode));

    }


}
