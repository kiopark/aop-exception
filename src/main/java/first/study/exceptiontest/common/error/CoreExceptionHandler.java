package first.study.exceptiontest.common.error;

import first.study.exceptiontest.common.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class CoreExceptionHandler {

  /**
   * @Valid 유효성검사에 inding 이 실패할 경우 handleMethodArgumentNotValidException 발생
   * HttpMessageConverter binding 을 못할 경우 발생
   * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * 지원하지 않는 HTTP method 호출 할 경우 발생
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    log.error("handleHttpRequestMethodNotSupportedException", e);
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * enum type 일치하지 않아 binding 못할 경우 발생
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMimatchException(MethodArgumentTypeMismatchException e) {
    log.error("handleMethodArgumentTypeMimatchException", e);
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }

}
