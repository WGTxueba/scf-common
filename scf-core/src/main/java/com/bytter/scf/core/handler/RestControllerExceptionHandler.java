package com.bytter.scf.core.handler;

import com.bytter.scf.core.exception.BusinessException;
import com.bytter.scf.core.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoul 2018/06/19
 */
@RestControllerAdvice
@ResponseBody
public class RestControllerExceptionHandler {

    protected Logger logger =  LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    /**
     * 处理访问控制器时，抛出的未知异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGlobalException(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        logger.error("发生未知或未捕获异常：\n{}",sw.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("message", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> allErrors = bindingResult.getFieldErrors();
        Map<String, Object> errorMap = new HashMap<>();
        List<Map<String, Object>> errors = new ArrayList<>();
        for(FieldError fieldError:allErrors){
            Map<String, Object> result = new HashMap<>();
            result.put("field",fieldError.getField());
            result.put("message",fieldError.getDefaultMessage());
            errors.add(result);
        }
        errorMap.put("type","validation_error");
        errorMap.put("errors",errors);
        logger.debug("数据验证错误：{}",errorMap);
        return errorMap;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBusinessExceptionException(BusinessException ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("type","business_error");
        errorMap.put("message", ex.getMessage());
        logger.debug("\n业务发生异常：{} \n异常抛出地方：{}",errorMap,stackTrace[0].toString());
        return errorMap;
    }

}
