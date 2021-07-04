package com.github.akazver.poc.controller;

import com.github.akazver.poc.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/hello/{string}")
    public String hello(@PathVariable String string) {
        return myService.hello(string);
    }

}
