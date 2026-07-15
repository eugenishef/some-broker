package dev.eshevchenko.dao;

import dev.eshevchenko.entity.Account;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

  @Query("SELECT a FROM Account a WHERE a.clientId = :clientId")
  List<Account> findAllByClientId(@Param("clientId") UUID clientId);
}