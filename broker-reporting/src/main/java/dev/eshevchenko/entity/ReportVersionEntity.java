package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "reporting", name = "report_versions")
@Getter
@Setter
public class ReportVersionEntity {

  @Id
  private UUID id;

  private UUID reportId;
  private int versionNumber;

  @JdbcTypeCode(SqlTypes.VARBINARY)
  @Column(columnDefinition = "bytea")
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