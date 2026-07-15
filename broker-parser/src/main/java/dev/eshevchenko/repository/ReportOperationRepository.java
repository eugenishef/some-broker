package dev.eshevchenko.repository;

import dev.eshevchenko.entity.ReportOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOperationRepository extends JpaRepository<ReportOperation, UUID> {
  List<ReportOperation> findByReportId(UUID reportId);
}