package dev.vinyeee.mysns.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void 회원가입이_정상적으로_동작하는경우(){

        String userName = "userName";
        String password = "password";

        // mocking



        Assertions.assertDoesNotThrow(() -> userService.signup(userName,password)); // assert: 실제 값과 예상 값을 비교하여 테스트의 성공여부를 결정







    }
}
