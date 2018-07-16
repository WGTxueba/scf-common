package com.bytter.scf.core.aspect;

import com.bytter.scf.core.exception.ValidateException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

//@Aspect
@Configuration
public class ValidateAspect {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Around("execution (* com.bytter.scf..controller.*.*(..))")
    public Object handleValidateResult(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("handleValidateResult");

        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult errors = (BindingResult) arg;
                if (errors.hasErrors()) {
                    throw new ValidateException(errors.getAllErrors());
                }
            }
        }
        return pjp.proceed();
    }

}
