package dev.eshevchenko.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

/**
 * Утилитные методы для чтения значений ячеек Excel (Apache POI).
 */
@Component
public class ExcelCellReaderUtils {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private final DataFormatter formatter = new DataFormatter();

  /**
   * Читает значение ячейки как UUID.
   * @param cell ячейка Excel
   * @return распарсенный {@link UUID}
   *
   * @throws IllegalArgumentException если содержимое ячейки не является корректным UUID
   *
   */
  public UUID readUuid(Cell cell) {
    return UUID.fromString(readString(cell));
  }

  /**
   * Читает значение ячейки как строку, удаляя пробелы по краям.
   * @param cell ячейка Excel
   * @return отформатированное строковое значение
   */
  public String readString(Cell cell) {
    return formatter.formatCellValue(cell).trim();
  }

  /**
   * Читает значение ячейки как денежную сумму, поддерживая
   * пробелы-разделители тысяч и запятую в качестве десятичного разделителя.
   * @param cell ячейка Excel
   * @return распарсенное значение {@link BigDecimal}
   *
   * @throws NumberFormatException если содержимое ячейки не является корректным числом
   */
  public BigDecimal readBigDecimal(Cell cell) {

    String value = formatter.formatCellValue(cell)
      .replace(" ", "")
      .replace(",", ".");

    return new BigDecimal(value);
  }

  /**
   * Читает значение ячейки как дату. Поддерживает как нативный числовой
   * формат даты Excel, так и текстовое представление в формате dd.MM.yyyy.
   * @param cell ячейка Excel
   * @return распарсенная {@link LocalDate}
   *
   * @throws java.time.format.DateTimeParseException если текстовое значение не соответствует формату dd.MM.yyyy
   *
   */
  public LocalDate readDate(Cell cell) {

    if (cell.getCellType() == CellType.NUMERIC) {
      return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    return LocalDate.parse(formatter.formatCellValue(cell), DATE_FORMAT);
  }

  /**
   * Проверяет, является ли строка пустой, основываясь на содержимом первой ячейки.
   * @param row строка Excel
   * @return {@code true}, если первая ячейка отсутствует или пуста
   */
  public boolean isEmpty(Row row) {
    Cell cell = row.getCell(0);
    return cell == null || formatter.formatCellValue(cell).isBlank();
  }
}