package site.xleon.platform.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data){
        return new Result<T>(ResultCodeEnum.SUCCESS.getValue(), ResultCodeEnum.SUCCESS.getLabel(), data);
    }

    public static <T> Result<T> fail(String message) {
        return Result.fail(ResultCodeEnum.WARN,  message, null);
    }

    public static <T> Result<T> fail(MyException exception) {
        ResultCodeEnum code = ResultCodeEnum.ERROR;
        if (exception.getCode() != null) {
            code = exception.getCode();
        }

        return Result.fail(code, exception.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCodeEnum code, String message, T data) {
        if (code == ResultCodeEnum.ERROR) {
            log.error("error: {}", message);
        } else {
            log.warn("warn: {}", message);
        }
        return new Result<>(code.getValue(), message, data);
    }
}

