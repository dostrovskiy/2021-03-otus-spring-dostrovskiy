package ru.otus.mybooks.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MyBookUserDetailsService userDetailsService;

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("reader").password("password").roles("READER")
                .and().withUser("admin").password("password").roles("READER", "ADMIN");
        //        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/mybooks/books/**").hasRole("READER")
                .antMatchers(HttpMethod.GET, "/mybooks/authors/**").hasRole("READER")
                .antMatchers(HttpMethod.GET, "/mybooks/genres/**").hasRole("READER")
                .antMatchers(HttpMethod.POST, "/mybooks/books").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/mybooks/books/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/mybooks/books/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/mybooks/books/**").hasRole("ADMIN")
        ;
    }
}
