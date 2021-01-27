package com.example.demodocker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
	
	@GetMapping(value = "/hello")
	public String getHello() {
		return "Hello World, Docker!!!";
	}
}
