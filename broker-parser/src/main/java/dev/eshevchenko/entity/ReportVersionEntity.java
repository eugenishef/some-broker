package dev.eshevchenko.entity;

import dev.eshevchenko.enums.ReportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "reporting", name = "report_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportVersionEntity extends BaseEntity {

  UUID reportId;
  int versionNumber;

  @Enumerated(EnumType.STRING)
  ReportStatus status;

  @JdbcTypeCode(SqlTypes.VARBINARY)
  @Column(name = "content")
  byte[] content;
}