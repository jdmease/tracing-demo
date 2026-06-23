package com.example.tracingdemo;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String hello() {
        return "Hello World";
    }

    public int sum() {
        int a = 500;
        throw new ArithmeticException();
        return 100 * a;
    }
}
