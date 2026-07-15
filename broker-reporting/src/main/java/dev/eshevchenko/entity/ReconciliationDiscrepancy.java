package dev.eshevchenko.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "reconciliation", name = "reconciliation_discrepancies")
@Getter
@Setter
public class ReconciliationDiscrepancy {

  @Id
  private UUID id;

  private UUID reconciliationId;
  private String field;
  private String expected;
  private String actual;

  @PrePersist
  protected void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }
}