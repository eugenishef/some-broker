package dev.eshevchenko.entity;

import dev.eshevchenko.enums.Status;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "dispute", name = "report_disputes")
@Getter
@Setter
public class Dispute {

  @Id
  private UUID id;
  private UUID reportId;

  @ElementCollection
  @CollectionTable(
    schema = "dispute",
    name = "report_dispute_operations",
    joinColumns = @JoinColumn(name = "dispute_id")
  )
  @Column(name = "operation_id")
  private List<UUID> operationIds;

  private String comment;

  @Enumerated(EnumType.STRING)
  private Status status;

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