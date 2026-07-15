package dev.eshevchenko.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.resource.transaction.spi.TransactionStatus;

@Entity
@Table(schema = "integration", name = "transactions")
@Getter
@Setter
public class Transaction {

  @Id
  private UUID id;

  private UUID clientId;
  private BigDecimal amount;
  private String currency;

  @Enumerated(EnumType.STRING)
  private TransactionStatus status;

  private Instant createdAt;

  @PrePersist
  protected void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}