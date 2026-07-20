package dev.eshevchenko.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import dev.eshevchenko.enums.ClientStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchClientRequest {

  @Min(0)
  int page = 0;

  @Min(1)
  @Max(100)
  int size = 20;

  @Builder.Default
  @JsonSetter(nulls = Nulls.AS_EMPTY)
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