package com.fastcampus.sns.controller;


import com.fastcampus.sns.controller.request.PostCreateRequest;
import com.fastcampus.sns.controller.request.PostModifyRequest;
import com.fastcampus.sns.controller.response.PostResponse;
import com.fastcampus.sns.controller.response.Response;
import com.fastcampus.sns.model.Post;
import com.fastcampus.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication){ // 유저를 받아오는건 spring security jwt 으로
        postService.create(postCreateRequest.getTitle(),postCreateRequest.getBody(),authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@RequestBody PostModifyRequest postModifyRequest, @PathVariable Integer postId, Authentication authentication){ // 유저를 받아오는건 spring security jwt 으로
        Post post = postService.modify(postModifyRequest.getTitle(),postModifyRequest.getBody(),authentication.getName(),postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication){
        postService.delete(authentication.getName(),postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication){ // 리스트 형태의 api 같은 경우 페이징이 필요하다
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication){ // 리스트 형태의 api 같은 경우 페이징이 필요하다
        return Response.success(postService.my(authentication.getName(),pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public  Response<Void> like(@PathVariable Integer postId , Authentication authentication){
        postService.like(postId,authentication.getName());
        return Response.success();
    }



}
