package site.xleon.platform.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyException extends Exception {

    private final ResultCodeEnum code;

    public MyException(String message, ResultCodeEnum code) {
        super(message);
        this.code = code;
    }

    public MyException(String message) {
        super(message);
        this.code = ResultCodeEnum.WARN;
    }
}
