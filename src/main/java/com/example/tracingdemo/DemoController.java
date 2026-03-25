package com.example.tracingdemo;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final Tracer tracer;

    public DemoController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("hello start");
        Span span = tracer.nextSpan().name("custom-hello-span").start();
        try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
            simulateProcessing();
            log.info("hello okay");
            return "Hello with tracing!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем состояние прерывания
            log.error("Thread was interrupted", e);
            return "Error";
        } finally {
            log.info("hello finally");
            span.end();
        }
    }

    private void simulateProcessing() throws InterruptedException {
        Thread.sleep(100); // Имитация обработки
    }
}
