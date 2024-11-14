package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    // TODO : implement
    public User signup(String userName, String password){
        // 회원가입하려는 userName 으로 회원가입된 user 가 있는지
        // 유저 등록 o => 예외

        //Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);

        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated",userName));
        });

        // 유저 등록 x => 회원가입 진행

//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName(userName);
//        userEntity.setPassword(password);
        //userEntityRepository.save(userEntity);
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, password));


        return User.fromEntity(userEntity);

    }

    // TODO :  implement
    // JWT: 그 유저가 어떤 유저인지 확인하기 위한 암호화된 문자열을 부여
    public String login(String userName, String password){ // 로그인에 성공하면 jwt 토근 부여

        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

        // 비밀번호 체크
        if (!userEntity.getPassword().equals(password)){
           throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"");
        }
        // 토큰 생성
        return "";
    }

}
