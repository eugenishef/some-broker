package dev.eshevchenko.i18n.swagger.annotations;

import dev.eshevchenko.doc.ClientExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation
@ApiResponses({
  @ApiResponse(responseCode = "201",
    description = "Клиент успешно создан",
    content = @Content(examples = @ExampleObject(value = ClientExamples.CREATE_CLIENT_RESPONSE))),
  @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
  @ApiResponse(responseCode = "409", description = "Клиент с таким email или ИНН уже существует"),
  @ApiResponse(responseCode = "403", description = "Недостаточно прав")
})
public @interface ApiClientCreated {

  @AliasFor(annotation = Operation.class, attribute = "summary")
  String summary() default "Создать клиента";

  @AliasFor(annotation = Operation.class, attribute = "description")
  String description() default "";
}