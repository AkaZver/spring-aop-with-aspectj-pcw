package com.github.akazver.poc.aop;

import com.github.akazver.poc.service.MyService;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@Aspect
public class ServiceAspect {

    @Autowired
    private MyService myService;

    @Before(value = "execution(* com.github.akazver.poc.service..*(..)) && args(string)",
            argNames = "string")
    public void execute(String string) {
        log.info("ServiceAspect executed with argument: {}", string);
        log.info("MyService is Spring AOP proxy: {}", AopUtils.isAopProxy(myService));
        log.info("MyService class name: {}", myService.getClass().getName());
    }

}
