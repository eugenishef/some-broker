package dev.eshevchenko.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "reporting", name = "reports")
@Getter
@Setter
public class Report extends AuditableEntity {

  private String type;
  private LocalDate periodFrom;
  private LocalDate periodTo;

  @JdbcTypeCode(SqlTypes.VARBINARY)
  @Column(columnDefinition = "bytea")
  private byte[] content;
}