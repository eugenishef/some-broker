package dev.eshevchenko.utils;

import dev.eshevchenko.dao.AccountRepository;
import dev.eshevchenko.entity.Account;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Утилитные методы для работы со счетами
 */
@Component
@RequiredArgsConstructor
public class AccountUtils {
  private final AccountRepository accountRepository;

  /**
   * Преобразует строковый accountId в UUID и возвращает счет.
   * Кидает исключение, если UUID некорректный или счет не найден.
   * @param accountId строковый идентификатор счета (UUID)
   * @return найденный счет {@link Account}
   */
  public Account getOrThrow(UUID accountId) {
    return accountRepository.findById(accountId)
      .orElseThrow(() -> new EntityNotFoundException("Счет не найден: " + accountId));
  }

  /**
   * Генерирует уникальный номер брокерского счета
   * Формат: 40817810XXXXXXXXXXX (11 цифр после префикса)
   * @return уникальный номер счета
   */
  public String generateAccountNumber() {
    return "40817810" + String.format("%011d", ThreadLocalRandom.current().nextLong(0, 100_000_000_000L));
  }
}