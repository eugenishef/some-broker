package dev.eshevchenko.parser.xlsx;

import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.exception.ReportImportException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class XlsxReportParser implements ReportExcelParser {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private final DataFormatter formatter = new DataFormatter();

  @Override
  public ReportImportData parse(MultipartFile file) {

    try (InputStream is = file.getInputStream();
      Workbook workbook = WorkbookFactory.create(is)) {
      Sheet reportSheet = workbook.getSheet("Report");

      if (reportSheet == null) {
        throw new ReportImportException("Лист 'Report' не найден");
      }

      Row metaRow = reportSheet.getRow(1);

      if (metaRow == null) {
        throw new ReportImportException("Лист 'Report' не содержит данных");
      }

      UUID clientId = readUuid(metaRow.getCell(0));
      String clientName = readString(metaRow.getCell(1));
      String inn = readString(metaRow.getCell(2));
      String reportType = readString(metaRow.getCell(3));
      LocalDate periodFrom = readDate(metaRow.getCell(4));
      LocalDate periodTo = readDate(metaRow.getCell(5));
      String currency = readString(metaRow.getCell(6));

      Sheet operationsSheet = workbook.getSheet("Operations");

      if (operationsSheet == null) {
        throw new ReportImportException("Лист 'Operations' не найден");
      }

      List<ReportOperationDto> operations = new ArrayList<>();

      for (Row row : operationsSheet) {
        if (row.getRowNum() == 0) {continue;}
        if (isEmpty(row)) {continue;}

        operations.add(
          new ReportOperationDto(
            readUuid(row.getCell(0)),
            readDate(row.getCell(1)),
            readBigDecimal(row.getCell(2)),
            readString(row.getCell(3)),
            readString(row.getCell(4)),
            readString(row.getCell(5))
          )
        );
      }

      return new ReportImportData(
        clientId,
        clientName,
        inn,
        reportType,
        periodFrom,
        periodTo,
        currency,
        operations
      );

    } catch (ReportImportException e) {
      throw e;
    } catch (Exception e) {
      throw new ReportImportException("Ошибка чтения XLSX", e);
    }
  }

  private UUID readUuid(Cell cell) {
    return UUID.fromString(readString(cell));
  }

  private String readString(Cell cell) {
    return formatter.formatCellValue(cell).trim();
  }

  private BigDecimal readBigDecimal(Cell cell) {

    String value = formatter.formatCellValue(cell)
      .replace(" ", "")
      .replace(",", ".");

    return new BigDecimal(value);
  }

  private LocalDate readDate(Cell cell) {

    if (cell.getCellType() == CellType.NUMERIC) {
      return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    return LocalDate.parse(formatter.formatCellValue(cell), DATE_FORMAT);
  }

  private boolean isEmpty(Row row) {
    Cell cell = row.getCell(0);
    return cell == null || formatter.formatCellValue(cell).isBlank();
  }
}