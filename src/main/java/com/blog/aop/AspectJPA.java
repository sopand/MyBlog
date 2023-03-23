package com.blog.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AspectJPA {
    @Pointcut("execution(* com.blog.controller.*.*(..)))")
    public void pointCut(){}
    @Pointcut("execution(* com.blog.service.*.*(..)))")
    public void serviceCut(){}

    @Around("execution(* com.blog.controller.*.*(..)))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("Controller 실행 메서드: {},실행 소요시간 {}ms",joinPoint.toString(),timeMs);
        }
    }
    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Object[] objects = joinPoint.getArgs();

        Arrays.stream(objects).forEach(obj -> log.info("들어온 파라미터 : "+obj));
        log.info("Controller 실행 메서드: {}",method.getName());
    }


    @AfterReturning(value = "serviceCut()",returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint,Object returnValue){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("Service에서 실행된 메소드:" +method.getName());
        log.info(method.getName()+" 메소드가 리턴한 값  : "+ returnValue);
    }

}
