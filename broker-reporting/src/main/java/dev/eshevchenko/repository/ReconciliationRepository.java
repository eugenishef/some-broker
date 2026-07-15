package dev.eshevchenko.repository;

import dev.eshevchenko.dto.view.DiscrepancyView;
import dev.eshevchenko.entity.Reconciliation;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, UUID> {

  @Query("""
        select
            d.field as field,
            d.expected as expected,
            d.actual as actual
        from ReconciliationDiscrepancy d
        where d.reconciliationId = :reconciliationId
        """)
  List<DiscrepancyView> findDiscrepancies(
    @Param("reconciliationId") UUID reconciliationId
  );
}