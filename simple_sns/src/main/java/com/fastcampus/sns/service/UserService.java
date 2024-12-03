package com.fastcampus.sns.service;


import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.User;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.UserEntityRepository;
import com.fastcampus.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional // 회원가입 중 exception 이 발생하게 되면 entity 를 save 하는 부분이 롤백
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


        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(userEntity);

    }

    // JWT: 그 유저가 어떤 유저인지 확인하기 위한 암호화된 문자열을 부여
    public String login(String userName, String password){ // 로그인에 성공하면 jwt 토근 부여

        // 회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found", userName)));

        // 비밀번호 체크
//        if (!userEntity.getPassword().equals(password))
        if(!encoder.matches(password,userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        // 토큰 생성
        String token = JwtTokenUtils.generateToken(userName,secretKey,expiredTimeMs);

        return token;
    }

    public User loadUserByUserName(String userName){
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }


}
