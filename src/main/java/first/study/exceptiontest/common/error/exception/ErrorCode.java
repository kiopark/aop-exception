package first.study.exceptiontest.common.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

  INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
  METHOD_NOT_ALLOWED(405, "C002", " Method Not Allowed");

//  @JsonValue
  private int status;
  private String code;
  private String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
