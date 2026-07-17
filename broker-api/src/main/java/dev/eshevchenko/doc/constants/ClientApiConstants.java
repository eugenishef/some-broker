package dev.eshevchenko.doc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientApiConstants {

  public static final String TAG_NAME = "Клиенты";
  public static final String TAG_DESCRIPTION = "Управление клиентами брокера";

  public static final String CREATE_CLIENT_SUMMARY = "Создать клиента";
  public static final String CREATE_CLIENT_DESCRIPTION = "Регистрирует нового клиента брокера.";

  public static final String GET_CLIENT_SUMMARY = "Получить клиента";
  public static final String GET_CLIENT_DESCRIPTION =
    "Возвращает подробную информацию о клиенте по его идентификатору, включая список открытых счетов.";

  public static final String SEARCH_CLIENTS_SUMMARY = "Поиск клиентов";
  public static final String SEARCH_CLIENTS_DESCRIPTION = """
    Возвращает постраничный список клиентов с возможностью фильтрации по email и статусу,
    а также сортировки по нескольким полям.
    """;

  public static final String UPDATE_CLIENT_SUMMARY = "Обновить клиента";
  public static final String UPDATE_CLIENT_DESCRIPTION = "Полное обновление данных клиента.";

  public static final String PATCH_CLIENT_SUMMARY = "Частично обновить данные клиента";
  public static final String PATCH_CLIENT_DESCRIPTION =
    "Обновляет только переданные поля клиента, остальные остаются без изменений.";

  public static final String BLOCK_CLIENT_SUMMARY = "Заблокировать клиента";
  public static final String BLOCK_CLIENT_DESCRIPTION = """
    Переводит клиента в статус BLOCKED с указанием причины блокировки.
    Доступные операции по счетам клиента будут ограничены.
    """;

  public static final String UNBLOCK_CLIENT_SUMMARY = "Разблокировать клиента";
  public static final String UNBLOCK_CLIENT_DESCRIPTION =
    "Возвращает клиента в статус ACTIVE и очищает причину блокировки.";

  public static final String GET_CLIENT_ACCOUNTS_SUMMARY = "Получить счета клиента";
  public static final String GET_CLIENT_ACCOUNTS_DESCRIPTION =
    "Возвращает список всех брокерских счетов, открытых на клиента, включая закрытые.";
}