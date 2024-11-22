package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.controller.request.PostModifyRequest;
import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.fixture.PostEntityFixture;
import dev.vinyeee.mysns.fixture.UserEntityFixture;
import dev.vinyeee.mysns.model.entity.PostEntity;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.PostEntityRepository;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void 포스트수정이_성공한_경우() throws Exception {

        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity)); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity)); // 수정하려는 포스트가 존재
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        // when -> then
        Assertions.assertDoesNotThrow(() -> postService.modify(title,body,userName,postId));



    }

    @Test
    public void 포스트수정시_포스트가_존재하지_않는경우() throws Exception {

        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity)); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty()); // 수정하려는 포스트가 존재하지 않음

        // when -> then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()-> postService.modify(title,body,userName,postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND,e.getErrorCode());

    }

    @Test
    public void 포스트수정시_권한이없는경우() throws Exception {

        String title = "title";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌

        UserEntity writer = UserEntityFixture.get("writer","password",2); // 실제 그 글을쓴 작성자

        // 로그인해서 수정 요청한 유저랑 postEntity 에서 꺼낸 유저(실제 작성자)가 다름
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer)); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity)); // 수정하려는 포스트가 존재


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()-> postService.modify(title,body,userName,postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION,e.getErrorCode());
    }


}
