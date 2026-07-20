package dev.eshevchenko.entity;

import dev.eshevchenko.enums.AccountStatus;
import dev.eshevchenko.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(schema = "api", name = "broker_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends BaseEntity {

  @Column(name = "client_id", nullable = false)
  UUID clientId;

  @Column(unique = true, nullable = false)
  String accountNumber;

  String currency;

  @Enumerated(EnumType.STRING)
  AccountType type;

  @Enumerated(EnumType.STRING)
  AccountStatus status;

  Instant openedAt;
  Instant closedAt;

  @PrePersist
  void prePersist() {
    openedAt = Instant.now();
    if (status == null) {
      status = AccountStatus.OPEN;
    }
  }
}