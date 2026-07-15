package dev.eshevchenko.dao;

import dev.eshevchenko.enums.ClientStatus;
import dev.eshevchenko.entity.Client;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

  boolean existsByInn(String inn);

  boolean existsByEmail(String email);

  @Query("""
      SELECT c FROM Client c
      WHERE (:email IS NULL OR lower(c.email) LIKE lower(concat('%', :email, '%')))
        AND (:status IS NULL or c.status = :status)
      """)
  Page<Client> search(@Param("email") String email,
    @Param("status") ClientStatus status,
    Pageable pageable);
}