package dev.eshevchenko.repository;


import dev.eshevchenko.entity.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  List<Transaction> findByClientIdAndCreatedAtBetween(
    UUID clientId, LocalDate from, LocalDate to);

  @Query("""
        select count(t) from Transaction t
        where t.clientId = :clientId
          and t.createdAt between :from and :to
        """)
  long countByClientAndPeriod(
    @Param("clientId") UUID clientId,
    @Param("from") LocalDate from,
    @Param("to") LocalDate to);
}