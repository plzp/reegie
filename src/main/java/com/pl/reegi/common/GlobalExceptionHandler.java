package com.pl.reegi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
//全局异常处理器
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//当个出现此异常时拦截处理
    public R exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String[] s = ex.getMessage().split(" ");
        if(ex.getMessage().contains("Duplicate entry")){
            return R.error("添加失败，"+s[2]+"已存在");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)//当个出现此异常时拦截处理
    public R exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
