package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema="reporting", name="report_operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportOperation {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name="report_id", nullable=false)
  private UUID reportId;

  @Column(name="operation_id", nullable=false)
  private UUID operationId;

  private LocalDate operationDate;
  private BigDecimal amount;
  private String currency;
  private String operationType;
  private String status;
}