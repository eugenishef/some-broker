package dev.eshevchenko.repository;

import dev.eshevchenko.entity.ReportVersionEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportVersionRepository extends JpaRepository<ReportVersionEntity, UUID> {

  List<ReportVersionEntity> findByReportIdOrderByVersionNumberAsc(UUID reportId);

  Optional<ReportVersionEntity> findByReportIdAndVersionNumber(UUID reportId, int versionNumber);

  int countByReportId(UUID reportId);

  Optional<ReportVersionEntity> findTopByReportIdOrderByVersionNumberDesc(UUID reportId);
}