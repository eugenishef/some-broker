package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "reporting", name = "report_versions")
@Getter
@Setter
public class ReportVersionEntity {

  @Id
  private UUID id;

  private UUID reportId;
  private int versionNumber;

  @Lob
  @Column(columnDefinition = "BYTEA")
  private byte[] content;

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