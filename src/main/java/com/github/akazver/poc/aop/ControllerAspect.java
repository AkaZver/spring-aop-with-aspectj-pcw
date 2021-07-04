package com.github.akazver.poc.aop;

import com.github.akazver.poc.controller.MyController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspect {

    private final MyController myController;

    @Before(value = "execution(* com.github.akazver.poc.controller..*(..)) && args(string)",
            argNames = "string")
    public void execute(String string) {
        log.info("ControllerAspect executed with argument: {}", string);
        log.info("MyController is Spring AOP proxy: {}", AopUtils.isAopProxy(myController));
        log.info("MyController class name: {}", myController.getClass().getName());
    }

}
