package dev.eshevchenko.entity;

import dev.eshevchenko.enums.Status;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditableEntity {

  @Id
  private UUID id;

  private UUID clientId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Version
  private Long version;

  @CreatedDate
  private Instant createdAt;

  @PrePersist
  protected void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }
}