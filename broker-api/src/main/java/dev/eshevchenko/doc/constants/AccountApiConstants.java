package dev.eshevchenko.doc.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountApiConstants {

  public static final String TAG_NAME = "Счета";
  public static final String TAG_DESCRIPTION = "Управление брокерскими счетами клиентов";

  public static final String CREATE_ACCOUNT_SUMMARY = "Открыть счет";
  public static final String CREATE_ACCOUNT_DESCRIPTION =
    "Открывает новый брокерский счет для существующего клиента с указанной валютой и типом.";

  public static final String CLOSE_ACCOUNT_SUMMARY = "Закрыть счет";
  public static final String CLOSE_ACCOUNT_DESCRIPTION = """
            Переводит счет в статус CLOSED и фиксирует дату закрытия.
            Операции по закрытому счету становятся недоступны.""";

  public static final String FREEZE_ACCOUNT_SUMMARY = "Заморозить счет";
  public static final String FREEZE_ACCOUNT_DESCRIPTION =
    "Временно ограничивает операции по счету без его закрытия";
}