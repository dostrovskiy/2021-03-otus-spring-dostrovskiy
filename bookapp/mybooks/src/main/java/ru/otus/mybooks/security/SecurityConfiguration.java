package ru.otus.mybooks.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;

import java.security.interfaces.RSAPublicKey;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MyBookUserDetailsService userDetailsService;

    @Value("${jwt.public.key}")
    private RSAPublicKey key;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringAntMatchers("/token"))
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .authorizeRequests(authz -> authz
                        .antMatchers(HttpMethod.POST, "/token").permitAll()
                        .antMatchers(HttpMethod.GET, "/mybooks/books/**").hasAnyAuthority("SCOPE_ROLE_READER", "SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.GET, "/mybooks/authors/**").hasAnyAuthority("SCOPE_ROLE_READER", "SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.GET, "/mybooks/genres/**").hasAnyAuthority("SCOPE_ROLE_READER", "SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.POST, "/mybooks/books/*/reviews").hasAnyAuthority("SCOPE_ROLE_READER", "SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.POST, "/mybooks/books").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.PUT, "/mybooks/books/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/mybooks/books/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/mybooks/books/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers(HttpMethod.GET, "/mybooks/books-all-info").hasAuthority("SCOPE_ROLE_ADMIN")
                        .antMatchers("/**").denyAll()
                );
    }


}
