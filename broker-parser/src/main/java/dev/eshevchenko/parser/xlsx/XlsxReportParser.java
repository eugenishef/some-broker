package dev.eshevchenko.parser.xlsx;

import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.exception.ReportImportException;
import dev.eshevchenko.utils.ExcelCellReaderUtils;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class XlsxReportParser implements ReportExcelParser {

  private final ExcelCellReaderUtils excelCellReaderUtils;

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

      UUID clientId = excelCellReaderUtils.readUuid(metaRow.getCell(0));
      String clientName = excelCellReaderUtils.readString(metaRow.getCell(1));
      String inn = excelCellReaderUtils.readString(metaRow.getCell(2));
      String reportType = excelCellReaderUtils.readString(metaRow.getCell(3));
      LocalDate periodFrom = excelCellReaderUtils.readDate(metaRow.getCell(4));
      LocalDate periodTo = excelCellReaderUtils.readDate(metaRow.getCell(5));
      String currency = excelCellReaderUtils.readString(metaRow.getCell(6));

      Sheet operationsSheet = workbook.getSheet("Operations");

      if (operationsSheet == null) {
        throw new ReportImportException("Лист 'Operations' не найден");
      }

      List<ReportOperationDto> operations = new ArrayList<>();

      for (Row row : operationsSheet) {
        if (row.getRowNum() == 0) {continue;}
        if (excelCellReaderUtils.isEmpty(row)) {continue;}

        operations.add(
          new ReportOperationDto(
            excelCellReaderUtils.readUuid(row.getCell(0)),
            excelCellReaderUtils.readDate(row.getCell(1)),
            excelCellReaderUtils.readBigDecimal(row.getCell(2)),
            excelCellReaderUtils.readString(row.getCell(3)),
            excelCellReaderUtils.readString(row.getCell(4)),
            excelCellReaderUtils.readString(row.getCell(5))
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
}