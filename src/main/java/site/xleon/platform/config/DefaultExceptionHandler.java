package site.xleon.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import site.xleon.platform.core.MyException;
import site.xleon.platform.core.Result;
import site.xleon.platform.core.ResultCodeEnum;
import site.xleon.platform.core.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Controller
@ControllerAdvice
public class DefaultExceptionHandler extends BasicErrorController {
    public DefaultExceptionHandler() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = this.getErrorAttributes(request, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.STACK_TRACE,
                ErrorAttributeOptions.Include.BINDING_ERRORS
        ));
        HttpStatus status = getStatus(request);
        String error = (String) body.get("error");
        if ("OK".equalsIgnoreCase(error)) {
            return null;
        }

        Result<Void> result;
        String message = (String) body.get("message");
        error += " " +  request.getMethod() + "@" + request.getRequestURI();
        if (status.value() >= 400) {
            error += " " + message;
            result = Result.fail(error);
            log.warn("container error: {} {} {}", request.getMethod(), request.getRequestURI(), result);
        } else {
            result = Result.fail(error + ": " + message);
            log.error("container error: {} {} {}", request.getMethod(), request.getRequestURI(), message);
        }

        Map<String, Object> map = Utils.objectToMap(result);
        map.remove("log");
        return new ResponseEntity<>(map, status);
    }

    @ResponseBody
    @ExceptionHandler
    public Result<Object> defaultException(HttpServletRequest request, Exception exception) {
        if (
                exception instanceof MethodArgumentTypeMismatchException ||
                exception instanceof MissingServletRequestParameterException ||
                exception instanceof HttpRequestMethodNotSupportedException ||
                exception instanceof HttpMessageConversionException
        ) {
            return Result.fail(exception.getMessage());
        } else if (exception instanceof MyException) {
            MyException e = (MyException) exception;
            return Result.fail(e);
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException invalidException = (MethodArgumentNotValidException) exception;
            FieldError filedError = invalidException.getBindingResult().getFieldError();
            assert filedError != null;
            return Result.fail(filedError.getField() + ": " + filedError.getDefaultMessage());
        }

        log.error("error: {} {}", request.getMethod(), request.getRequestURI(), exception);
        return Result.fail(ResultCodeEnum.ERROR, exception.getMessage(), null);
    }
}
