package dev.eshevchenko.repository;

import dev.eshevchenko.entity.ReconciliationDiscrepancy;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationDiscrepancyRepository extends JpaRepository<ReconciliationDiscrepancy, UUID> {
}