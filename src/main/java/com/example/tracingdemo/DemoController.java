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

    private final Tracer tracer;
    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    public DemoController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/hello")
    public String hello() {
        log.info("Processing /hello request");
        Span span = tracer.nextSpan().name("custom-hello-span").start();
        try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
            Thread.sleep(100); // Имитация обработки
            return "Hello with tracing!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while processing /hello", e);
            throw new RuntimeException("Request processing was interrupted", e);
        } finally {
            span.end();
        }
    }
}