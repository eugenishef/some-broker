package dev.eshevchenko.dto.view;

import java.util.UUID;

public interface ClientData {
  UUID getId();
  String getFullName();
  String getInn();
  String getEmail();
}