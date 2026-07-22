package dev.eshevchenko.repository;

import dev.eshevchenko.entity.ReportOperation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOperationRepository extends JpaRepository<ReportOperation, UUID> {

  @Modifying
  @Query("DELETE FROM ReportOperation o WHERE o.reportId = :reportId")
  void deleteAllByReportId(@Param("reportId") UUID reportId);
}