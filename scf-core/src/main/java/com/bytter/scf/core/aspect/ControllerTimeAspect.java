package com.bytter.scf.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 控制器时间切面
 */
@Aspect
@Configuration
public class ControllerTimeAspect {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };


    @Around("execution (* com.bytter.scf..controller.*.*(..))")
    public Object controller(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        if(servletRequestAttributes!=null){
            request = servletRequestAttributes.getRequest();
        }else{
            throw new RuntimeException("request can't be null");
        }

        long beginTime = new Date().getTime();
        Object object = point.proceed();
        long endTime = new Date().getTime();


        StringBuilder sb = new StringBuilder("\n");
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        sb.append("------------------------ Bytter Controller Report ------------------------------\n");
        sb.append("--------------------------- ").append(sdf.get().format(endTime)).append(" --------------------------------\n");
        sb.append("Uri            : ").append(request.getRequestURI()).append("\n");
        sb.append("Controller     : ").append(point.getSignature().getDeclaringType().getName()).append("\n");
        sb.append("Method         : ").append(methodSignature.getName()).append("\n");
        sb.append("Request Method : ").append(request.getMethod()).append("\n");
        getParams(request, sb);
        sb.append("----------------------------- Take Time:").append(endTime - beginTime).append("ms ----------------------------------\n");
        logger.debug(sb.toString());
        return object;
    }

    private void getParams(HttpServletRequest request, StringBuilder sb) {
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter      : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=");
                    sb.append(values[0]);
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
    }
}
