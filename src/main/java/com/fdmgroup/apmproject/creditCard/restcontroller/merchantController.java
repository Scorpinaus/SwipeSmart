package com.fdmgroup.apmproject.creditCard.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class merchantController {
	@GetMapping("/purchase")
	public String purchase() {
		return "purchase";
	}
}
