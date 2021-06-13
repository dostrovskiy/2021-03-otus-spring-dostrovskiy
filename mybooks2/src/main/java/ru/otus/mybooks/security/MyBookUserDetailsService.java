package ru.otus.mybooks.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.mybooks.repositories.AccountRepository;

@Service
@RequiredArgsConstructor
public class MyBookUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var account = accountRepository.findAccountByUserName(name).orElseThrow(() -> new UsernameNotFoundException(name));
        return User.builder()
                .username(account.getUserName())
                .password(account.getPassword())
                .authorities("READER")
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
