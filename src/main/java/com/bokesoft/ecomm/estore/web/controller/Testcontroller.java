package com.bokesoft.ecomm.estore.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Testcontroller {
	
	@RequestMapping(value = "test")
	public String test() {
		return "test";
	}
}
