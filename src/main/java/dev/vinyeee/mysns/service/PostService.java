package dev.vinyeee.mysns.service;

import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.model.Post;
import dev.vinyeee.mysns.model.entity.PostEntity;
import dev.vinyeee.mysns.model.entity.UserEntity;
import dev.vinyeee.mysns.repository.PostEntityRepository;
import dev.vinyeee.mysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){

        // user find
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",userName)));

        // post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title,body,userEntity));
        // return // 작성한 post 를 굳이 넘겨줄 필요는 없을 것 같아서 void 로 유지
    }


    @Transactional
    public Post modify(String title, String body, String userName, Integer postId){
        // 로그인 했는지 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",userName)));

        // 포스트가 존재하는지 체크
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not found",postId))
        );

        // 자신이 작성한 포스트가 맞으면 수정
        if (postEntity.getUser() != userEntity){ // 포스트 작성한 유저랑 로그인한 유저랑 비교
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

       return Post.fromEntity(postEntityRepository.save(postEntity));
    }
}
