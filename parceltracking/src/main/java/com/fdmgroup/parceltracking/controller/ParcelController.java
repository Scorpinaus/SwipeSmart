package com.fdmgroup.parceltracking.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.parceltracking.model.Customer;
import com.fdmgroup.parceltracking.model.Location;
import com.fdmgroup.parceltracking.model.Parcel;
import com.fdmgroup.parceltracking.model.Status;
import com.fdmgroup.parceltracking.service.LocationService;
import com.fdmgroup.parceltracking.service.ParcelService;
import com.fdmgroup.parceltracking.service.StatusService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ParcelController {

	@Autowired
	private ParcelService parcelService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private StatusService statusService;

	private static Logger logger = LogManager.getLogger(ParcelController.class);

	@GetMapping("/dashboard/parcels")
	public String parcelPage() {
		return "parcel";
	}

	@GetMapping("/dashboard/parcels/success")
	public String createParcel() {
		return "parcelcreated";
	}

	@GetMapping("/dashboard/parcels/1")
	public String parcel1Page(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", customer);
		return "parcel1";
	}

	@GetMapping("/dashboard/parcels/2")
	public String parcel2Page(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", customer);
		return "parcel2";
	}

	@GetMapping("/dashboard/parcels/3")
	public String parcel3Page(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", customer);
		return "parcel3";
	}

	@GetMapping("/dashboard/parcels/4")
	public String parcel4Page(HttpSession session, Model model) {
		Customer customer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", customer);
		return "parcel4";
	}

	@PostMapping("/dashboard/parcels/1")
	public String purchaseParcel1(@RequestParam(required = false, name = "shippingAddress") String shippingAddress,
			@RequestParam("creditCard") String creditCard, @RequestParam("creditCardPIN") String pin, Model model,
			HttpSession session) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		if (creditCard.length() != 19 || pin.length() != 3) {
			model.addAttribute("customer", tempCustomer);
			logger.warn("Invalid credit card number");
			return "redirect:/dashboard/parcels/1";
		} else {
			Location tempLocation = locationService.findByLocation("Singapore", "Singapore");
			Status tempStatus = statusService.findByStatus("Out for delivery");
			Parcel tempParcel = new Parcel(tempCustomer, tempLocation, tempStatus, "iPhone 13 128GB Storage",
					"/iphone13.jpg");
			tempParcel.setCreditCardNumber(creditCard.substring(15));
			if (shippingAddress.isEmpty()) {
				shippingAddress = tempCustomer.getAddress();
				tempParcel.setShippingAddress(shippingAddress);
			} else {
				tempParcel.setShippingAddress(shippingAddress);
			}
			parcelService.persist(tempParcel);
			logger.info("Parcel created and added to Customer");
			return "parcel";
		}
	}

	@PostMapping("/dashboard/parcels/2")
	public String purchaseParcel2(@RequestParam(required = false, name = "shippingAddress") String shippingAddress,
			@RequestParam("creditCard") String creditCard, @RequestParam("creditCardPIN") String pin,
			HttpSession session, Model model) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		if (creditCard.length() != 19 || pin.length() != 3) {
			model.addAttribute("customer", tempCustomer);
			logger.warn("Invalid credit card number");
			return "redirect:/dashboard/parcels/2";
		} else {
			Location tempLocation = locationService.findByLocation("Singapore", "Singapore");
			Status tempStatus = statusService.findByStatus("Reaching Local Facility");
			Parcel tempParcel = new Parcel(tempCustomer, tempLocation, tempStatus, "240W Apple USB-C Charge Cable (2m)",
					"/appleChargingCable.jpeg");
			tempParcel.setCreditCardNumber(creditCard.substring(15));
			if (shippingAddress.isEmpty()) {
				shippingAddress = tempCustomer.getAddress();
				tempParcel.setShippingAddress(shippingAddress);
			} else {
				tempParcel.setShippingAddress(shippingAddress);
			}
			parcelService.persist(tempParcel);
			model.addAttribute("tempParcel", tempParcel);
			logger.info("Parcel created and added to Customer");
			return "parcel";
		}
	}

	@PostMapping("/dashboard/parcels/3")
	public String purchaseParcel3(@RequestParam(required = false, name = "shippingAddress") String shippingAddress,
			@RequestParam("creditCard") String creditCard, @RequestParam("creditCardPIN") String pin,
			HttpSession session, Model model) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		if (creditCard.length() != 19 || pin.length() != 3) {
			model.addAttribute("customer", tempCustomer);
			logger.warn("Invalid credit card number");
			return "redirect:/dashboard/parcels/2";
		} else {
			Location tempLocation = locationService.findByLocation("Singapore", "Singapore");
			Status tempStatus = statusService.findByStatus("Out for delivery");
			Parcel tempParcel = new Parcel(tempCustomer, tempLocation, tempStatus,
					"Samsung 970 EVO Plus NVMe M.2 SSD 2TB", "/samsungSSD.jpg");
			tempParcel.setCreditCardNumber(creditCard.substring(15));
			if (shippingAddress.isEmpty()) {
				shippingAddress = tempCustomer.getAddress();
				tempParcel.setShippingAddress(shippingAddress);
			} else {
				tempParcel.setShippingAddress(shippingAddress);
			}
			parcelService.persist(tempParcel);
			model.addAttribute("tempParcel", tempParcel);
			logger.info("Parcel created and added to Customer");
			return "parcel";
		}
	}

	@PostMapping("/dashboard/parcels/4")
	public String purchaseParcel4(@RequestParam(required = false, name = "shippingAddress") String shippingAddress,
			@RequestParam("creditCard") String creditCard, @RequestParam("creditCardPIN") String pin,
			HttpSession session, Model model) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		if (creditCard.length() != 19 || pin.length() != 3) {
			model.addAttribute("customer", tempCustomer);
			logger.warn("Invalid credit card number");
			return "redirect:/dashboard/parcels/2";
		} else {
			Location tempLocation = locationService.findByLocation("China", "Shenzhen");
			Status tempStatus = statusService.findByStatus("Shipping from Origin");
			Parcel tempParcel = new Parcel(tempCustomer, tempLocation, tempStatus, "MSI GeForce RTX 3070 GAMING X TRIO",
					"/MSIRTX3070.jpg");
			tempParcel.setCreditCardNumber(creditCard.substring(15));
			if (shippingAddress.isEmpty()) {
				shippingAddress = tempCustomer.getAddress();
				tempParcel.setShippingAddress(shippingAddress);
			} else {
				tempParcel.setShippingAddress(shippingAddress);
			}
			parcelService.persist(tempParcel);
			model.addAttribute("tempParcel", tempParcel);
			logger.info("Parcel created and added to Customer");
			return "parcel";
		}
	}
}
