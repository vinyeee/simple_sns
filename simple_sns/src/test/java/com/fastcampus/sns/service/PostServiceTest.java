package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.fixture.PostEntityFixture;
import com.fastcampus.sns.fixture.UserEntityFixture;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.PostEntityRepository;
import com.fastcampus.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// 서비스단에서는 비즈니스 로직만 테스트, 로그인관련은 컨트롤러
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

    @Test
    public void 포스트삭제가_성공한_경우() throws Exception {

        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity)); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity)); // 수정하려는 포스트가 존재

        // when -> then
        Assertions.assertDoesNotThrow(() -> postService.delete(userName,postId));

    }

    @Test
    public void 포스트삭제시_포스트가_존재하지_않는경우() throws Exception {

        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity)); // userName 으로 찾은 유저가 일단 존재해야함
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty()); // 수정하려는 포스트가 존재하지 않음

        // when -> then
        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,()-> postService.delete(userName,postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND,e.getErrorCode());

    }

    @Test
    public void 포스트삭제시_권한이없는경우() throws Exception {

        String userName = "userName";
        Integer postId = 1;

        // mocking
        PostEntity postEntity = PostEntityFixture.get(userName,postId,1);// userName 과 postId 로 특정한 post 객체를 만들어줌
        UserEntity writer = UserEntityFixture.get("writer","password",2); // 실제 그 글을쓴 작성자

        // 로그인해서 수정 요청한 유저랑 postEntity 에서 꺼낸 유저(실제 작성자)가 다름
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()-> postService.delete(userName,postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION,e.getErrorCode());
    }

    @Test
    public void 피드목록요청이_성공한_경우() throws Exception {

        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));

    }

    @Test
    public void 내피드목록요청이_성공한_경우() throws Exception {


        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = mock(UserEntity.class);

        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findAllByUser(userEntity,pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my("",pageable));

    }



}
