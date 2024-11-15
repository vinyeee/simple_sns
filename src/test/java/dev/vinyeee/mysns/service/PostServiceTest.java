package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.model.entity.PostEntity;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.PostEntityRepository;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;// 실제 동작 하지 않고 원하는 동작만을 정의하여 테스트할 수 있도록 함

    @MockBean
    private UserEntityRepository userEntityRepository;// 실제 동작 하지 않고 원하는 동작만을 정의하여 테스트할 수 있도록 함

    @Test
    public void 포스트작성이_성공한_경우(){

        //give
        String title = "title";
        String body = "body";
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class))); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // when -> then
        Assertions.assertDoesNotThrow(() -> postService.create(title,body,userName));
    }

    @Test
    public void 포스트작성시_요청한_유저가_존재하지_않는경우(){

        //give
        String title = "title";
        String body = "body";
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty()); // userName 으로 찾은 유저가 없어야함
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        // when -> then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()-> postService.create(title,body,userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND,e.getErrorCode());
    }
}
