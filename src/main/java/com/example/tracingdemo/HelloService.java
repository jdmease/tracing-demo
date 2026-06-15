package com.example.tracingdemo;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String hello() {
        return "Hello World";
    }

    public void printBye() {
        return "Bye";
    }
}
