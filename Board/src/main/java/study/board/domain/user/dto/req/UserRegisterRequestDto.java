package study.board.domain.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequestDto(

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "문자열은 소문자와 숫자로만 구성되며, 길이는 4자 이상 10자 이하여야 합니다.")
    String userName,

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "문자열은 대소문자 알파벳과 숫자로만 구성되며, 길이는 8자 이상 15자 이하여야 합니다.")
    String password

    // 이슈사항 : 요청에 "비밀번호확인" 이라는 변수를 추가해야 하는가...? 그리고 백엔드에서도 검증해야 하는가?
    // 결론 : 애초에 요청에 포함시키지 않는다. (클라이언트 폼 유효성 검사이기 때문, 단순 오타검사. 요청값의 validation과는 무관)
    // 참조 : https://stackoverflow.com/questions/42190530/matching-passwords-with-front-end-or-back-end
) {



}