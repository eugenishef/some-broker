package dev.eshevchenko.utils;

import dev.eshevchenko.dao.ClientRepository;
import dev.eshevchenko.entity.Client;
import dev.eshevchenko.exceptions.EntityNotFoundException;
import dev.eshevchenko.exceptions.InvalidClientIdException;
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
  public Client getOrThrow(UUID clientId) {
    return repository.findById(clientId)
      .orElseThrow(() -> new EntityNotFoundException("Клиент с id=" + clientId + " не найден"));
  }
}