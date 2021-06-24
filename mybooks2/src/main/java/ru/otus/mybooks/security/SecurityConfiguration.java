package ru.otus.mybooks.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MyBookUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/mybooks/books/**").hasAnyRole("READER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/mybooks/authors/**").hasAnyRole("READER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/mybooks/genres/**").hasAnyRole("READER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/mybooks/books/*/reviews").hasAnyRole("READER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/mybooks/books").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/mybooks/books/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/mybooks/books/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/mybooks/books/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/mybooks/books-all-info").hasRole("ADMIN")
                .antMatchers("/**").denyAll()
        ;
    }
}
