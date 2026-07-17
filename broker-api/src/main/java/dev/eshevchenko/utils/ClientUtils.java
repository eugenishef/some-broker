package dev.eshevchenko.utils;

import dev.eshevchenko.dao.ClientRepository;
import dev.eshevchenko.entity.Client;
import dev.eshevchenko.exceptions.InvalidClientIdException;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Утилитные методы для работы с клиентами
 */
@Component
@RequiredArgsConstructor
public class ClientUtils {
  private final ClientRepository repository;

  /**
   * Возвращает клиента по его идентификатору.
   * @param clientId строковый идентификатор клиента (UUID)
   * @return найденный клиент {@link Client}
   *
   * @throws InvalidClientIdException если формат UUID некорректный
   * @throws EntityNotFoundException  если клиент не найден
   */
  public Client getOrThrow(String clientId) {
    UUID id = parseClientId(clientId);
    return repository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Клиент не найден: " + clientId));
  }

  /**
   * Преобразует строковый идентификатор клиента в UUID.
   * @param clientId строковый UUID клиента
   * @return объект {@link UUID}
   *
   * @throws InvalidClientIdException если строка имеет некорректный формат UUID
   */
  public UUID parseClientId(String clientId) {
    try {
      return UUID.fromString(clientId);
    } catch (IllegalArgumentException ex) {
      throw new InvalidClientIdException("Некорректный формат идентификатора клиента: " + clientId);
    }
  }
}