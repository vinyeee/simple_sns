package dev.vinyeee.mysns.controller;

import dev.vinyeee.mysns.controller.request.PostCreateRequest;
import dev.vinyeee.mysns.controller.response.Response;
import dev.vinyeee.mysns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest postCreateRequest, Authentication authentication){ // 유저를 받아오는건 spring security jwt 으로
        postService.create(postCreateRequest.getTitle(),postCreateRequest.getBody(),authentication.getName());
        return Response.success(null);
    }
}
