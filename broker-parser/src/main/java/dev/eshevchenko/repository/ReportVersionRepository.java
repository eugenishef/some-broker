package dev.eshevchenko.repository;

import dev.eshevchenko.entity.ReportVersionEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportVersionRepository extends JpaRepository<ReportVersionEntity, UUID> {

  @Query("SELECT MAX(v.versionNumber) FROM ReportVersionEntity v WHERE v.reportId = :reportId")
  Optional<Integer> findMaxVersionNumberByReportId(@Param("reportId") UUID reportId);

  @Modifying
  @Query("UPDATE ReportVersionEntity v SET v.actual = false WHERE v.reportId = :reportId AND v.actual = true")
  void clearActualFlag(@Param("reportId") UUID reportId);
}