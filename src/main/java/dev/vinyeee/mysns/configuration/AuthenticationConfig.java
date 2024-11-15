package dev.vinyeee.mysns.configuration;

import dev.vinyeee.mysns.configuration.filter.JwtTokenFilter;
import dev.vinyeee.mysns.util.JwtTokenUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/*/users/signup","/api/*/users/login").permitAll()// 유저의 로그인이나 회원가입은 항상 허용
                .antMatchers("/api/**").authenticated() // 그 이외의 것들은 항상 인증필요
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class) // 요청이 들어올 때마다 요청 앞에서 필터를 통해 그 안에 있는 토큰 값을 보고 어떤 유저인지 확인

                ;
                // TODO
//                .exceptionHandling() // security 인증 과정에서 exception 이 발생했을 경우에
//                .authenticationEntryPoint()
    }
}
