package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    // TODO : implement
    public User signup(String userName, String password){
        // 회원가입하려는 userName 으로 회원 가입된 user 가 있는지
        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName)

        // 유저 등록 x => 회원가입 진행
        userEntityRepository.save(new UserEntity());

        //userEntityRepository.save();
        // 유저 등록 o => 예외


        return new User();

    }

    // TODO :  implement
    // JWT: 그 유저가 어떤 유저인지 확인하기 위한 암호화된 문자열을 부여
    public String login(){ // 로그인에 성공하면 jwt 토근 부여
        return "";
    }

}
