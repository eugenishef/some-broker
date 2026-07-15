package dev.eshevchenko.entity;

import dev.eshevchenko.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

@Entity
@Table(schema = "reporting", name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report implements Persistable<UUID> {

  @Id
  private UUID id;

  @Column(name = "client_id")
  private UUID clientId;

  @Column(name = "client_name")
  private String clientName;

  private String inn;

  @Column(nullable = false)
  private String type;

  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  @Column(name = "period_from")
  private LocalDate periodFrom;

  @Column(name = "period_to")
  private LocalDate periodTo;

  private String currency;

  @Version
  private Long version;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Transient
  @Builder.Default
  private boolean isNew = true;

  @PrePersist
  void prePersist(){
    createdAt = LocalDateTime.now();
  }

  @PostLoad
  @PostPersist
  void markNotNew(){
    isNew = false;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}