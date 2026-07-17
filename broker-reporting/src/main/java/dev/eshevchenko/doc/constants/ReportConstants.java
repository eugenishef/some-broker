package dev.eshevchenko.doc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportConstants {

  public static final String TAG_NAME = "Отчеты";
  public static final String TAG_DESCRIPTION = "Формирование, получение и дополнение отчетов по клиентам";

  public static final String CREATE_REPORT_SUMMARY = "Создать запрос на отчет";
  public static final String CREATE_REPORT_DESCRIPTION =
    "Создает новый отчет со статусом PENDING и запускает его асинхронную генерацию в фоне через Kafka.";

  public static final String SEARCH_REPORTS_SUMMARY = "Поиск отчетов";
  public static final String SEARCH_REPORTS_DESCRIPTION =
    "Возвращает постраничный список отчетов с фильтрацией по клиенту, типу и статусу.";

  public static final String GET_REPORT_SUMMARY = "Получить отчет";
  public static final String GET_REPORT_DESCRIPTION =
    "Возвращает данные отчета по его идентификатору, включая текущий статус и версию.";

  public static final String GET_REPORT_VERSIONS_SUMMARY = "Получить версии отчета";
  public static final String GET_REPORT_VERSIONS_DESCRIPTION =
    "Возвращает список всех бизнес-версий отчета, созданных при его первичной генерации и последующих дополнениях.";

  public static final String GET_REPORT_VERSION_SUMMARY = "Получить конкретную версию отчета";
  public static final String GET_REPORT_VERSION_DESCRIPTION =
    "Возвращает содержимое и дату создания конкретной бизнес-версии отчета по её номеру.";

  public static final String GET_REPORT_PDF_SUMMARY = "Скачать PDF отчета";
  public static final String GET_REPORT_PDF_DESCRIPTION =
    "Возвращает сгенерированный PDF-файл отчета для скачивания. Доступно только для отчетов в статусе READY.";
}