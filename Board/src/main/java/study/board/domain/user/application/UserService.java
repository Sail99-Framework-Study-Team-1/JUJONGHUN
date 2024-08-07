package study.board.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.user.domain.User;
import study.board.domain.user.dto.req.UserRegisterRequestDto;
import study.board.domain.user.dto.res.UserRegisterResponseDto;
import study.board.domain.user.repo.UserRepository;
import study.board.global.error.ErrorCode;
import study.board.global.error.exception.GlobalException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto){
        if (userRepository.existsUserByUserName(requestDto.userName())) {
            throw new GlobalException(ErrorCode.USER_IS_ALREADY_EXIST);
        }

        User user = createUser(requestDto);
        User savedUser = userRepository.save(user);

        return new UserRegisterResponseDto(savedUser.getId());
    }





    private User createUser(UserRegisterRequestDto requestDto){
        return User.builder()
            .userName(requestDto.userName())
            .password(passwordEncoder.encode(requestDto.password()))
            .build();
    }

}
