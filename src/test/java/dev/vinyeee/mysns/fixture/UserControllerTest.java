package dev.vinyeee.mysns.fixture;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinyeee.mysns.controller.request.UserJoinRequest;
import dev.vinyeee.mysns.controller.request.UserLoginRequest;
import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.exception.SnsApplicationException;
import dev.vinyeee.mysns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 객체를 사용해 HTTP 요청과 응답을 시뮬레이션
public class UserControllerTest {

    //HTTP 요청과 응답을 실제 서버 없이도 테스트할 수 있도록 도와줌
    @Autowired
    private MockMvc mockMvc;

    //spring 에서 관리하는 bean을 mock 객체로 대체 (가짜 빈)
    @MockBean
    private UserService userservice;


    @Autowired
    private ObjectMapper objectMapper;


//    @Test
//    public void 회원가입() throws Exception {
//        String userName = "userName";
//        String password = "password";
//
//        when(userservice.signup(userName,password)).thenReturn(mock(User.class));
//
//
//        mockMvc.perform(post("/api/v1/users/join")
//                .contentType(MediaType.APPLICATION_JSON)
//                // TODO : add request body
//                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password)))
//        ).andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//
//    @Test
//    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우() throws Exception {
//
//        String userName = "userName";
//        String password = "password";
//
//        when(userservice.signup(userName,password)).thenThrow(new SnsApplicationException());
//
//
//        mockMvc.perform(post("/api/v1/users/join")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password)))
//        ).andDo(print())
//                .andExpect(status().isConflict());
//    }
//
//
//    @Test
//    public void 로그인() throws Exception {
//        String userName = "userName";
//        String password = "password";
//
//        when(userservice.login(userName,password)).thenReturn("test_token");
//
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // TODO : add request body
//                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
//                ).andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void 로그인시_회원가입이_안된_username을_입력할경우_에러반환() throws Exception {
//        String userName = "userName";
//        String password = "password";
//
//        when(userservice.login(userName,password)).thenThrow(new SnsApplicationException());
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // TODO : add request body
//                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
//                ).andDo(print())
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void 로그인시_password가_틀린경우_에러반환() throws Exception {
//        String userName = "userName";
//        String password = "password";
//
//        when(userservice.login(userName, password)).thenThrow(new SnsApplicationException());
//
//
//        mockMvc.perform(post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // TODO : add request body
//                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//
//    }
//


}
