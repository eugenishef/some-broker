package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "reporting", name = "reports")
@Getter
@Setter
public class Report extends AuditableEntity {

  private String type;
  private LocalDate periodFrom;
  private LocalDate periodTo;

  @Lob
  @Column(columnDefinition = "BYTEA")
  private byte[] content;
}