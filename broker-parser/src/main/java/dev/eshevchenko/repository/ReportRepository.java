package dev.eshevchenko.repository;

import dev.eshevchenko.entity.Report;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
  Optional<Report> findByClientIdAndTypeAndPeriodFromAndPeriodTo(
    UUID clientId, String type, LocalDate periodFrom, LocalDate periodTo);
}