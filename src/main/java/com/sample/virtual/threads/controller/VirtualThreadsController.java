package com.sample.virtual.threads.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class VirtualThreadsController {

    private final AtomicLong counter = new AtomicLong();

    // 🧵 Shows request thread type + simulates blocking (sleep)
    @GetMapping("/vt/hello")
    public String hello(@RequestParam(defaultValue = "200") long ms) throws InterruptedException {
        long id = counter.incrementAndGet();
        Thread.sleep(ms); // simulate blocking I/O
        return "\tid=" + id
                + " | thread=" + Thread.currentThread()
                + " | isVirtual=" + Thread.currentThread().isVirtual();
    }

    // 🧵 Fan-out: start many virtual threads (demo)
    @GetMapping("/vt/fanout")
    public String fanout(@RequestParam(defaultValue = "1000") int tasks,
                         @RequestParam(defaultValue = "50") long workMs) throws InterruptedException {

        long start = System.nanoTime();
        CountDownLatch latch = new CountDownLatch(tasks);

        for (int i = 0; i < tasks; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(workMs); // simulate blocking I/O
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long tookMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

        return "Done fanout tasks=" + tasks
                + " | workMs=" + workMs
                + " | tookMs=" + tookMs;
    }
}