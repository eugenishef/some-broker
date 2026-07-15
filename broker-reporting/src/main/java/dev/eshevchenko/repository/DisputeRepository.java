package dev.eshevchenko.repository;

import dev.eshevchenko.entity.Dispute;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, UUID> {
}