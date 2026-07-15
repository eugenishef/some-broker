package dev.eshevchenko.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "reconciliation", name = "reconciliations")
@Getter
@Setter
public class Reconciliation extends AuditableEntity {
  private LocalDate periodFrom;
  private LocalDate periodTo;
}