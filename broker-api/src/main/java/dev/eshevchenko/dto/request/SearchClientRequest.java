package dev.eshevchenko.dto.request;

import dev.eshevchenko.enums.ClientStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchClientRequest {

  @Min(0)
  int page = 0;

  @Min(1)
  @Max(100)
  int size = 20;

  List<SortField> sort = new ArrayList<>();

  ClientFilter filter;

  @Getter
  @Setter
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class SortField {
    String field;
    Sort.Direction direction;
  }

  @Getter
  @Setter
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ClientFilter {
    String email;
    ClientStatus status;
  }
}