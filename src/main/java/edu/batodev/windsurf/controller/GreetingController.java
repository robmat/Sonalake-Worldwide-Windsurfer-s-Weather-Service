package edu.batodev.windsurf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greet")
public class GreetingController {

    @GetMapping
    public Greeting greet() {
        return new Greeting("Hello from Windsurf!");
    }

    record Greeting(String msg) {}
}