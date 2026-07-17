package dev.eshevchenko.repository;

import dev.eshevchenko.dto.ClientData;
import dev.eshevchenko.entity.ClientSummary;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository
  extends JpaRepository<ClientSummary, UUID> {

  @Query("""
        select
            c.id as id,
            c.fullName as fullName,
            c.inn as inn,
            c.email as email
        from ClientSummary c
        where c.id = :clientId
        """)
  Optional<ClientData> fetchClientData(
    @Param("clientId") UUID clientId
  );
}