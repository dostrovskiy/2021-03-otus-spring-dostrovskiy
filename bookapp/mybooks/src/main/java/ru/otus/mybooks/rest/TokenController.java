package ru.otus.mybooks.rest;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    @Value("${jwt.private.key}")
    private RSAPrivateKey key;

    @PostMapping("/token")
    public String token(Authentication authentication) {
        var now = Instant.now();
        var expiry = 60L * 60L * 1L; // 1 hour
        var scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        var claims = new JWTClaimsSet.Builder()
                .issuer("mybook")
                .issueTime(new Date(now.toEpochMilli()))
                .expirationTime(new Date(now.plusSeconds(expiry).toEpochMilli()))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        var header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
        var jwt = new SignedJWT(header, claims);
        return sign(jwt).serialize();
    }

    private SignedJWT sign(SignedJWT jwt) {
        try {
            jwt.sign(new RSASSASigner(this.key));
            return jwt;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}
