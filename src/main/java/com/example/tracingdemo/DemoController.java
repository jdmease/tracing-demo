package com.example.tracingdemo;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST контроллер для демонстрации Micrometer Tracing.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final Tracer tracer;

    /**
     * Конструктор класса DemoController.
     *
     * @param tracer экземпляр Tracer для создания спанов
     */
    public DemoController(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Обрабатывает GET запрос на /api/hello и возвращает сообщение с трассировкой.
     *
     * @return строка приветствия или сообщение об ошибке
     */
    @GetMapping("/hello")
    public String hello() {
        log.info("Starting hello endpoint processing");
        Span span = tracer.nextSpan().name("custom-hello-span").start();
        try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
            Thread.sleep(100); // Имитация обработки
            log.info("Successfully processed hello request");
            return "Hello with tracing!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted during sleep", e);
            throw new RuntimeException(e);
        } finally {
            log.info("Finishing hello endpoint processing");
            span.end();
        }
    }
}