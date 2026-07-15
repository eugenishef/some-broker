package dev.eshevchenko.i18n.swagger.annotations;

import io.swagger.v3.oas.annotations.Operation;
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
  @ApiResponse(responseCode = "204", description = "Операция выполнена успешно"),
  @ApiResponse(responseCode = "404", description = "Ресурс не найден")
})
public @interface ApiNoContentOrNotFound {

  @AliasFor(annotation = Operation.class, attribute = "summary")
  String summary() default "";

  @AliasFor(annotation = Operation.class, attribute = "description")
  String description() default "";
}