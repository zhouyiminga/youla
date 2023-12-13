package com.zhou.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class WebExceptionHandler {


    /**
     * 自定义校验参数异常的返回参数 zhou
     */
    @ExceptionHandler(org.springframework.validation.BindException.class)
    @ResponseBody
    public ResultVO MethodArgumentNotValidExceptionHandler(org.springframework.validation.BindException e) {
        log.error(e.toString());
        ResultVO resultVO=new ResultVO();
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        resultVO.setResultCode("0001");
        resultVO.setResultMessage(message);
        return resultVO;
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResultVO handleIOException(IOException ex) {
        log.error(ex.toString());
        ResultVO resultVO=new ResultVO();
        resultVO.setResultCode("00001");
        resultVO.setResultMessage(ex.getMessage());
        return resultVO;
    }
}
