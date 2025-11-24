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
    public String hello() throws InterruptedException {
        log.info("Processing hello request");
        Span span = tracer.nextSpan().name("custom-hello-span").start();
        try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
            Thread.sleep(100); // Имитация обработки
            log.info("Hello processed successfully");
            return "Hello with tracing!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Request processing was interrupted", e);
            throw e; // Пробрасываем исключение, чтобы вызывающий код мог корректно отреагировать
        } finally {
            span.end();
        }
    }
}