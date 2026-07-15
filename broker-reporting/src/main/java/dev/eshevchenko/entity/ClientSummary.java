package dev.eshevchenko.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "dictionary", name = "clients")
@Getter
@Setter
public class ClientSummary {

  @Id
  private UUID id;

  private String fullName;
  private String inn;
  private String email;
}