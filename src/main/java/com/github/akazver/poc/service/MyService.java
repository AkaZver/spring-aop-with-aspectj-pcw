package com.github.akazver.poc.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {

    public String hello(String string) {
        return "Hello, " + string;
    }

}
