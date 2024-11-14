package dev.vinyeee.mysns.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
                ;
                // TODO
//                .exceptionHandling() // security 인증 과정에서 exception 이 발생했을 경우에
//                .authenticationEntryPoint()
    }
}
