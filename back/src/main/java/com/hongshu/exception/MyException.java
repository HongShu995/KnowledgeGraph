package com.hongshu.exception;

import com.hongshu.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.hongshu.enums.StatusCodeEnum.FAIL;
/**
 * 业务异常
 *
 * @author HongShu995
 * @create 2022-01-14
 */
@Getter
@AllArgsConstructor
public class MyException extends RuntimeException
{
    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public MyException(String message)
    {
        this.message = message;
    }

    public MyException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMessage();
    }
}
