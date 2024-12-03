package dev.vinyeee.mysns.configuration.filter;

import dev.vinyeee.mysns.model.User;
import dev.vinyeee.mysns.service.UserService;
import dev.vinyeee.mysns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter { // 이 필터는 모든 HTTP 요청마다 한 번씩 실행됨 (Spring Security의 필터 체인에 등록)

    private final String key;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 필터 내부를 구현하는 메서드 (모든 요청마다 실행됨)

        // 1. 요청 헤더에서 Authorization 헤더를 가져옴
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2. Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면, 로그를 남기고 필터 체인의 다음 필터로 넘어감
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header. header is null or invalid");
            filterChain.doFilter(request, response); // 다음 필터로 진행
            return; // 더 이상 처리하지 않음
        }

        try {

            // 3. "Bearer " 이후의 실제 토큰 값을 추출
            final String token = header.split(" ")[1].trim(); // "Bearer " 이후의 문자열을 가져옴

            // 4. 토큰 유효성 검사
            // TODO: check token is valid (토큰이 만료되었거나 유효하지 않은지 확인)
            if(JwtTokenUtils.isExpired(token,key)){
                log.error("key is expired");
                filterChain.doFilter(request, response); // 왜 만료된 필터에도 doFilter 를 ?
                // 만약 doFilter()를 호출하지 않고 단순히 return으로 종료한다면, 나머지 필터들이 실행되지 않아 보안 로직이 제대로 작동하지 않을 수 있음

                return; // 더 이상 처리하지 않음
            }

            // 5. 추출한 토큰에서 사용자 이름을 가져옴
            // TODO: get username from token
            String userName = JwtTokenUtils.getUserName(token, key); // 토큰에서 사용자 이름 추출

            // 6. 사용자 이름이 유효한지 확인
            // 유저가 실제로 존재하는지
            // TODO: check the userName is valid
            User user = userService.loadUserByUserName(userName);



            // 7. 인증된 사용자 정보를 생성하여 SecurityContext에 저장
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    // 실제 사용자 정보 (userDetails)와 권한 리스트를 추가할 수 있음
                    // 현재는 인증 객체를 null로 설정 (사용자 정보와 권한이 없기 때문)
                    user, null, user.getAuthorities()
            );


            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            // 9. 예외가 발생한 경우 로그를 남기고 필터 체인의 다음 필터로 진행
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        // 10. 정상적인 경우, 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
