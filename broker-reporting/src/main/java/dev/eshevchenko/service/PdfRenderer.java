package dev.eshevchenko.service;

import dev.eshevchenko.dto.ClientData;
import dev.eshevchenko.dto.TransactionData;
import java.util.List;

public interface PdfRenderer {
  byte[] render(ClientData clientData, List<TransactionData> transactions);
}