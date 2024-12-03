package dev.vinyeee.mysns.configuration;

import dev.vinyeee.mysns.configuration.filter.JwtTokenFilter;
import dev.vinyeee.mysns.exception.CustomAuthenticationEntryPoint;
import dev.vinyeee.mysns.service.UserService;
import dev.vinyeee.mysns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().regexMatchers("^(?!/api/).*");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/*/users/join","/api/*/users/login").permitAll()// 유저의 로그인이나 회원가입은 항상 허용
                .antMatchers("/api/**").authenticated() // 그 이외의 것들은 항상 인증필요
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용할 때는 서버에서 세션을 관리할 필요가 없음
                .and()
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class) // 요청이 들어올 때마다 JWT 토큰을 확인하고, 유효성을 검사하는 필터
                .exceptionHandling() // security 인증 과정에서 exception 이 발생했을 경우에
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); //
    }
}
