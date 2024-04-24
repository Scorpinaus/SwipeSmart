package com.fdmgroup.apmproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is a Spring MVC controller responsible for handling requests related to merchants.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Controller
public class merchantController {
	
	/**
	 * Retrieves the purchase page.
	 * <p>
	 * This method handles GET requests to "/purchase" endpoint, returning the purchase page.
	 *
	 * @return The name of the purchase page as a String.
	 * @see org.example.StatusService#purchase()
	 */
	@GetMapping("/purchase")
	public String purchase() {
		return "purchase";
	}
}
