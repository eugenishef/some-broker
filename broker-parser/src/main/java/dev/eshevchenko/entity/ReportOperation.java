package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(schema="reporting", name="report_operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportOperation extends BaseEntity {

  @Column(name="report_id", nullable=false)
  UUID reportId;

  @Column(name="operation_id", nullable=false)
  UUID operationId;

  LocalDate operationDate;
  BigDecimal amount;
  String currency;
  String operationType;
  String status;
}