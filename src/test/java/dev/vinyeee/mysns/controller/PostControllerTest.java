package dev.vinyeee.mysns.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinyeee.mysns.controller.request.PostCreateRequest;
import dev.vinyeee.mysns.controller.request.PostModifyRequest;
import dev.vinyeee.mysns.exception.ErrorCode;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser // 로그인 된 사용자
    public void 포스트작성() throws Exception{

        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
        ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser // 로그인 하지않은 익명의 유저로 요청을 날렸을 경우
    public void 포스트작성시_로그인을_하지않은_경우() throws Exception{
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    public void 포스트수정() throws Exception {

        String title = "title";
        String body = "body";

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @WithAnonymousUser // 로그인 하지않은 익명의 유저로 요청을 날렸을 경우
    public void 포스트수정시_로그인을_하지않은_경우() throws Exception{
        String title = "title";
        String body = "body";

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND)).when(postService).modify(eq(title), eq(body),any(),eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception{
        String title = "title";
        String body = "body";

        // mocking (modify 가 void를 return 하기 때문에 when으로 mocking 불가 when은 실제로 호출하고나서 예외를 던지지만 doThorw는 호출없이 던짐 )
        // 본인이 작성한 글이 아니기때문에 postService 의 modify 가 호출되면 INVALID_PERMISSION 에러가 떠야함
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body),any(),eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void 포스트수정시_수정하려는_글이_없는경우_에러발생() throws Exception{
        String title = "title";
        String body = "body";

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body),any(),eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title,body)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }



}
