package ru.otus.mybooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.mybooks.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUserName(String name);
}
