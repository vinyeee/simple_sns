package dev.vinyeee.mysns.controller;

import dev.vinyeee.mysns.controller.request.PostCreateRequest;
import dev.vinyeee.mysns.controller.request.PostModifyRequest;
import dev.vinyeee.mysns.controller.response.PostResponse;
import dev.vinyeee.mysns.controller.response.Response;
import dev.vinyeee.mysns.model.Post;
import dev.vinyeee.mysns.service.PostService;
import lombok.RequiredArgsConstructor;
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
}
