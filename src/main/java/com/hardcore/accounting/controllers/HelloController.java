package com.hardcore.accounting.controllers;

import java.util.concurrent.atomic.AtomicLong;
import com.hardcore.accounting.model.Greeting;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private AtomicLong counter = new AtomicLong();

    @GetMapping("v1/greeting")
    public Greeting sayHello(@RequestParam("name") String name) {
        return new Greeting(counter.incrementAndGet(), String.format("Hello, %s", name));
    }

}
