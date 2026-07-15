package dev.eshevchenko.repository;

import dev.eshevchenko.entity.Report;
import dev.eshevchenko.enums.Status;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

  Optional<Report> findByClientIdAndPeriodFromAndPeriodTo(
    UUID clientId, LocalDate periodFrom, LocalDate periodTo);

  @Query("""
        select r from Report r
        where (:clientId is null or r.clientId = :clientId)
          and (:type is null or r.type = :type)
          and (:status is null or r.status = :status)
        """)
  Page<Report> search(
    @Param("clientId") UUID clientId,
    @Param("type") String type,
    @Param("status") Status status,
    Pageable pageable);
}