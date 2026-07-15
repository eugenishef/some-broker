package dev.eshevchenko.service.impl;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dev.eshevchenko.dto.ClientData;
import dev.eshevchenko.dto.TransactionData;
import dev.eshevchenko.exception.ReportRenderingException;
import dev.eshevchenko.service.PdfRenderer;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.lowagie.text.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenPdfReportRenderer implements PdfRenderer {

  private static final DateTimeFormatter DATE_FORMAT =
    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());

  @Override
  public byte[] render(ClientData clientData, List<TransactionData> transactions) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Document document = new Document(PageSize.A4, 36, 36, 54, 36);
      PdfWriter.getInstance(document, out);
      document.open();

      addHeader(document, clientData);
      addTransactionsTable(document, transactions);

      document.close();
      return out.toByteArray();
    } catch (DocumentException | IOException ex) {
      log.error("Ошибка генерации PDF", ex);
      throw new ReportRenderingException("Не удалось сгенерировать PDF отчета", ex);
    }
  }

  private void addHeader(Document document, ClientData clientData) throws DocumentException {
    Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
    Font infoFont = new Font(Font.HELVETICA, 11);

    Paragraph title = new Paragraph("Отчет по клиенту", titleFont);
    title.setSpacingAfter(12);
    document.add(title);

    document.add(new Paragraph("Клиент: " + clientData.fullName(), infoFont));
    document.add(new Paragraph("ИНН: " + clientData.inn(), infoFont));
    document.add(new Paragraph("Email: " + clientData.email(), infoFont));

    Paragraph spacer = new Paragraph(" ");
    spacer.setSpacingAfter(16);
    document.add(spacer);
  }

  private void addTransactionsTable(Document document, List<TransactionData> transactions)
    throws DocumentException {
    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{3, 2, 2, 2});

    Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    addHeaderCell(table, "Дата", headerFont);
    addHeaderCell(table, "Сумма", headerFont);
    addHeaderCell(table, "Валюта", headerFont);
    addHeaderCell(table, "Статус", headerFont);

    Font cellFont = new Font(Font.HELVETICA, 10);
    for (TransactionData tx : transactions) {
      table.addCell(new PdfPCell(new Phrase(DATE_FORMAT.format(tx.createdAt()), cellFont)));
      table.addCell(new PdfPCell(new Phrase(tx.amount().toPlainString(), cellFont)));
      table.addCell(new PdfPCell(new Phrase(tx.currency(), cellFont)));
      table.addCell(new PdfPCell(new Phrase(tx.status(), cellFont)));
    }

    if (transactions.isEmpty()) {
      PdfPCell emptyCell = new PdfPCell(new Phrase("Нет операций за период", cellFont));
      emptyCell.setColspan(4);
      table.addCell(emptyCell);
    }

    document.add(table);
  }

  private void addHeaderCell(PdfPTable table, String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setBackgroundColor(new Color(60, 63, 65));
    cell.setPadding(6);
    table.addCell(cell);
  }
}