package dev.eshevchenko.entity;

import dev.eshevchenko.enums.ClientStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(schema = "api", name = "clients")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  String firstName;
  String lastName;
  String middleName;
  LocalDate birthDate;

  @Column(unique = true, nullable = false)
  String email;

  String phone;

  @Column(unique = true)
  String inn;

  @Embedded
  Passport passport;

  @Enumerated(EnumType.STRING)
  ClientStatus status;

  String blockReason;

  Instant createdAt;
  Instant updatedAt;

  @PrePersist
  void prePersist() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    createdAt = Instant.now();
    updatedAt = Instant.now();
    if (status == null) {
      status = ClientStatus.ACTIVE;
    }
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = Instant.now();
  }
}