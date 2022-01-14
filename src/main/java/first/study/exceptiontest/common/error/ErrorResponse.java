package first.study.exceptiontest.common.error;

import first.study.exceptiontest.common.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private int status;
  private String code;
  private String message;
  private List<FieldError> errors;

  private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
    this.status = errorCode.getStatus();
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.errors = errors;
  }

  private ErrorResponse(ErrorCode errorCode) {
    this.status = errorCode.getStatus();
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.errors = new ArrayList<>();
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }

  public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
    return new ErrorResponse(errorCode, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
    return new ErrorResponse(errorCode, errors);
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
    String value = e.getName() == null ? "" : e.getValue().toString();
    List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
    return new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, errors);
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FieldError {
    private String field;
    private String value;
    private String reason;

    private FieldError(String field, String value, String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    private static List<FieldError> of(String field, String value, String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));
      return fieldErrors;
    }

    private static List<FieldError> of(BindingResult bindingResult) {
      List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()
          ))
          .collect(Collectors.toList());
    }

  }
}
