package dev.eshevchenko.entity;

import dev.eshevchenko.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

@Builder
@Entity
@Table(schema = "reporting", name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report extends BaseEntity implements Persistable<UUID> {

  @Column(name = "client_id")
  UUID clientId;

  @Column(name = "client_name")
  String clientName;

  String inn;

  @Column(nullable = false)
  String type;

  @Enumerated(EnumType.STRING)
  ReportStatus status;

  @Column(name = "period_from")
  LocalDate periodFrom;

  @Column(name = "period_to")
  LocalDate periodTo;

  String currency;

  @Version
  Long version;

  @Transient
  @Builder.Default
  boolean isNew = true;

  @Override
  public UUID getId() {
    return super.getId();
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  @PostLoad
  @PostPersist
  void markNotNew(){
    isNew = false;
  }
}