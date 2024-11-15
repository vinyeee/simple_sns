package dev.vinyeee.mysns.service;


import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.fixture.UserEntityFixture;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    public void 회원가입이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";

        // mocking: userEntityRepository.findByUserName() 메서드가 호출되었을 때 실제 데이터베이스를 조회하는 대신, 우리가 지정한 fixture 데이터를 반환
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); // userName에 해당하는 계정이 없으면 무조건 empty 여야함
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName,password)); // save 하면 저장된 엔티티를 반환


        Assertions.assertDoesNotThrow(() -> userService.signup(userName,password)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정


    }

    @Test
    public void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우(){

        // given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName,password);

        // when
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName,password)); // save 하면 저장된 엔티티를 반환

        // then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,() -> userService.signup(userName,password)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME,e.getErrorCode());

    }

    @Test
    public void 로그인이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";

        // 가상의 entity userEntity Fixture
        UserEntity fixture = UserEntityFixture.get(userName,password);

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture)); //<optional>UserEntity 객체를 전달해야하는데 null 이 아니여서 of로 감싸줌
        when(encoder.matches(password,fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(userName,password)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정


    }

    @Test
    public void 로그인시_userName으로_가입된_계정이_없는경우(){

        String userName = "userName";
        String password = "password";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); // userName에 해당하는 계정이 없으면 무조건 empty 여야함

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,() -> userService.login(userName,password)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());


    }

    @Test
    public void 로그인시_userName으로_가입된_계정은_있지만_패스워드가_틀린경우() {

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(wrongPassword, fixture.getPassword())).thenReturn(false);

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());


    }
}
