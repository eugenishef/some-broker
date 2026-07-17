package dev.eshevchenko.doc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportApiConstants {
  public static final String TAG_NAME = "Импорт отчетов";
  public static final String TAG_DESCRIPTION = "Загрузка отчетов из внешних файлов";

  public static final String IMPORT_REPORT_SUMMARY = "Импортировать отчет из файла";
  public static final String IMPORT_REPORT_DESCRIPTION = "Загружает файл отчета (например, CSV или Excel) и создает на его основе новый отчет с батчевой обработкой содержимого.";
}