package com.hongshu.handler;

import com.hongshu.exception.MyException;
import com.hongshu.vo.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.hongshu.enums.StatusCodeEnum.SYSTEM_ERROR;
import static com.hongshu.enums.StatusCodeEnum.VALID_ERROR;

/**
 * 全局异常处理
 *
 * @author HongShu995
 * @create 2022-01-20
 */
@RestControllerAdvice
public class ControllerAdviceHandler
{
    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = MyException.class)
    public Result<?> errorHandler(MyException e)
    {
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> errorHandler(MethodArgumentNotValidException e)
    {
        return Result.fail(VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> errorHandler(Exception e) {
        e.printStackTrace();
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMessage());
    }
}
