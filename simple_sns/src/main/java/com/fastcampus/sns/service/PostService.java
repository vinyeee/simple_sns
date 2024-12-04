package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.Post;
import com.fastcampus.sns.model.entity.CommentEntity;
import com.fastcampus.sns.model.entity.LikeEntity;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.CommentEntityRepository;
import com.fastcampus.sns.repository.LikeEntityRepository;
import com.fastcampus.sns.repository.PostEntityRepository;
import com.fastcampus.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){

        // user find
        UserEntity userEntity = getUserOrException(userName);

        // post save
        PostEntity saved = postEntityRepository.save(PostEntity.of(title,body,userEntity));
        // return // 작성한 post 를 굳이 넘겨줄 필요는 없을 것 같아서 void 로 유지
    }


    @Transactional
    public Post modify(String title, String body, String userName, Integer postId){
        // 로그인 했는지 체크
        UserEntity userEntity = getUserOrException(userName);

        // 포스트가 존재하는지 체크
        PostEntity postEntity = getPostOrException(postId);
        // 자신이 작성한 포스트가 맞으면 수정
        if (postEntity.getUser() != userEntity){ // 포스트 작성한 유저랑 로그인한 유저랑 비교
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

//       return Post.fromEntity(postEntityRepository.save(postEntity));
        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    public void delete(String userName, Integer postId){
        UserEntity userEntity = getUserOrException(userName);
        PostEntity postEntity = getPostOrException(postId);

        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,String.format("%s has no permission with %s",userName,postId));
        }

        postEntityRepository.delete(postEntity);

    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity); // map 함수를 이용하여 Page<PostEntity> 에 있는 엔티티들을 Post 로 변경해준다
    }

    public Page<Post> my(String userName, Pageable pageable){

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        return postEntityRepository.findAllByUser(userEntity,pageable).map(Post::fromEntity); // map 함수를 이용하여 Page<PostEntity> 에 있는 엔티티들을 Post 로 변경해준다
    }

    @Transactional
    public void like(Integer postId, String userName){

        // 포스트가 존재하는지 체크
        PostEntity postEntity = getPostOrException(postId);
        // 유저 체크
        UserEntity userEntity = getUserOrException(userName);

        // 좋아요 눌렀는지 체크 - > Throw
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED,String.format("userName %s already like post %d",userName,postId));
        });

        // 누른 적 없으면 save
        likeEntityRepository.save(LikeEntity.of(userEntity,postEntity));

    }

    @Transactional
    public int likeCount(Integer postId){

        // 포스트가 존재하는지 체크
        PostEntity postEntity = getPostOrException(postId);

        // 좋아요 갯수 세기
//        List<LikeEntity> allByPost = likeEntityRepository.findAllByPost(postEntity);
//        return allByPost.size();
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment){
        PostEntity postEntity = getPostOrException(postId);
        UserEntity userEntity = getUserOrException(userName);



    }


    private PostEntity getPostOrException(Integer postId){
        // 포스트가 존재하는지 체크
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND,String.format("%s not found",postId))
        );
    }

    private UserEntity getUserOrException(String userName){
        // 유저가 존재하는지 체크
        return userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not found",userName)));
    }
}
