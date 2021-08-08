package ru.otus.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.bookstore.domain.Sale;

@RepositoryRestResource(path = "bookstore")
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
