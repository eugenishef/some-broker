package dev.eshevchenko.doc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReconciliationConstants {

  public static final String TAG_NAME = "Сверка";
  public static final String TAG_DESCRIPTION = "Сверка операций и поиск расхождений";

  public static final String START_RECONCILIATION_SUMMARY = "Запустить сверку";
  public static final String START_RECONCILIATION_DESCRIPTION = """
    Запускает асинхронную сверку операций клиента за указанный период.
    Обработка выполняется в фоне, статус доступен через отдельный эндпоинт.
    """;

  public static final String GET_RECONCILIATION_STATUS_SUMMARY = "Получить статус сверки";
  public static final String GET_RECONCILIATION_STATUS_DESCRIPTION =
    "Возвращает текущий статус сверки: ожидает выполнения, выполняется, завершена или завершена с ошибкой.";

  public static final String GET_RECONCILIATION_RESULT_SUMMARY = "Получить различия сверки";
  public static final String GET_RECONCILIATION_RESULT_DESCRIPTION = """
    Возвращает список найденных расхождений по завершенной сверке.
    Для незавершенной сверки список будет пустым.
    """;
}