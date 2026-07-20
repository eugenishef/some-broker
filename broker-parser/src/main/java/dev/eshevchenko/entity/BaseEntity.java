package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

  @Id
  @UuidGenerator(style = Style.TIME)
  @Column(updatable = false, nullable = false)
  UUID id;

  @Column(name = "created_at", updatable = false, nullable = false)
  Instant createdAt;

  @PrePersist
  void onPrePersist() {
    createdAt = Instant.now();
  }
}