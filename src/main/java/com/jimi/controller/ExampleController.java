package com.jimi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

	@RequestMapping("/hello")
    String home() {
        return "Hello World!";
    }

	
}
