package dev.vinyeee.mysns.configuration.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter { // 이 필터는 모든 HTTP 요청마다 한 번씩 실행됨 (Spring Security의 필터 체인에 등록)

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
            // 3. 토큰 유효성 검사
            // TODO: check token is valid (토큰이 만료되었거나 유효하지 않은지 확인)

            // 4. "Bearer " 이후의 실제 토큰 값을 추출
            final String token = header.split(" ")[1].trim(); // "Bearer " 이후의 문자열을 가져옴

            // 5. 추출한 토큰에서 사용자 이름을 가져옴
            // TODO: get username from token
            String userName = ""; // 토큰에서 사용자 이름 추출

            // 6. 사용자 이름이 유효한지 확인
            // TODO: check the userName is valid

            // 7. 인증된 사용자 정보를 생성하여 SecurityContext에 저장
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    // 실제 사용자 정보 (userDetails)와 권한 리스트를 추가할 수 있음
                    // 현재는 인증 객체를 null로 설정 (사용자 정보와 권한이 없기 때문)
                    null, null, null
            );

            // 8. SecurityContext에 인증된 사용자 정보를 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (RuntimeException e) {
            // 9. 예외가 발생한 경우 로그를 남기고 필터 체인의 다음 필터로 진행
            filterChain.doFilter(request, response);
            log.error("Error occurs while validating. {}", e.toString());
            return;
        }

        // 10. 정상적인 경우, 필터 체인의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
