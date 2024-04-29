package com.fdmgroup.parceltracking.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.parceltracking.model.Customer;
import com.fdmgroup.parceltracking.model.Parcel;
import com.fdmgroup.parceltracking.service.CustomerService;
import com.fdmgroup.parceltracking.service.ParcelService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ParcelService parcelService;

	private static Logger logger = LogManager.getLogger(CustomerController.class);

	@GetMapping("/index")
	public String welcomePage() {
		return "index";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboardPage(HttpSession session, Model model) {
		if (session.getAttribute("loggedUser") != null) {
			Customer returnedCustomer = (Customer) session.getAttribute("loggedUser");
			model.addAttribute("customer", returnedCustomer);
			return "dashboard";
		} else {
			return "redirect:/login";
		}
	}

	@GetMapping("/logout")
	public String logoutPage(HttpSession session) {
		logger.info("Customer has logged out.");
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("customers/{id}")
	public String profilePage(@PathVariable("id") long customerId, Model model) {
		logger.info("Redirecting to Customer's profile page");
		Customer customer = customerService.findCustomerById(customerId);
		model.addAttribute("customer", customer);
		model.addAttribute("profileUrl", customer.getProfileUrl());
		model.addAttribute("backgroundUrl", customer.getBackgroundUrl());
		return "profile";
	}

	@GetMapping("customers/{id}/details")
	public String editProfilePage(@PathVariable("id") long customerId, HttpSession session, Model model) {
		logger.info("Editing Customer's profile page");
		Customer returnedCustomer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", returnedCustomer);
		return "details";
	}

	@GetMapping("/customers/{id}/collected")
	public String collectedParcelPage(@PathVariable("id") long customerId) {
		return "parcelcollected";
	}

	@GetMapping("/customers/{id}/parcel{parcelId}details")
	public String parcelDetailsPage(@PathVariable("id") long customerId, @PathVariable("parcelId") long parcelId,
			HttpSession session, Model model) {
		session.setAttribute("parcel", parcelService.findById(parcelId));
		Customer returnedCustomer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", returnedCustomer);
		logger.info("Parcel added to Session");
		return "parceldetails";
	}

	@GetMapping("/loginerror")
	public String loginErrorPage() {
		return "loginerror";
	}

	@GetMapping("/admindashboard")
	public String adminDashboardPage() {
		return "admindashboard";
	}

	@GetMapping("/customers/{id}/profilepicture")
	public String profilePictureChangePage(@PathVariable("id") long customerId, HttpSession session, Model model) {
		Customer returnedCustomer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", returnedCustomer);
		return "profilepicture";
	}

	@GetMapping("/customers/{id}/backgroundpicture")
	public String backgroundPictureChangePage(@PathVariable("id") long customerId, HttpSession session, Model model) {
		Customer returnedCustomer = (Customer) session.getAttribute("loggedUser");
		model.addAttribute("customer", returnedCustomer);
		return "backgroundpicture";
	}

	@PostMapping("/register")
	public String processRegistration(@RequestParam("username") String username,
			@RequestParam("password") String password, Model model) {
		Customer customer = new Customer(null, username, password, null, null, "/profilepicture.png",
				"/homePageBackground.jpg");
		if (customerService.usernameInDatabase(username) || username.equals("") || password.equals("")) {
			model.addAttribute("error", true);
			logger.warn("Invalid username or password to register");
			return ("register");
		} else {
			customerService.persist(customer);
			return ("redirect:/login");
		}
	}

	@PostMapping("/login")
	public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session, Model model) {
		if (customerService.passwordChecker(username, password)) {
			logger.info("Authentication was successful!");
			session.setAttribute("loggedUser", customerService.findCustomerByUsername(username));
			return "redirect:/dashboard";
		} else if (username.equals("admin") && password.equals("admin")) {
			logger.info("Authentication successful! Welcome admin!");
			return "redirect:/admindashboard";
		} else {
			logger.warn("Authentication failed!");
			model.addAttribute("error", true);
			return "login";
		}
	}

	@PostMapping("/customers/{id}/details")
	public String editCustomerProfile(@PathVariable("id") long customerId,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName, HttpSession session, Model model) {
		Customer tempCustomer = customerService.findCustomerById(customerId);
		if (!address.isEmpty()) {
			tempCustomer.setAddress(address);
		}
		if (!firstName.isEmpty()) {
			tempCustomer.setFirstName(firstName);
		}
		if (!lastName.isEmpty()) {
			tempCustomer.setLastName(lastName);
		}
		session.setAttribute("loggedUser", tempCustomer);
		model.addAttribute("customer", tempCustomer);
		customerService.update(tempCustomer);
		return "redirect:/customers/{id}";
	}

	@PostMapping("/customers/{id}")
	public String parcelCollectedByCustomer(@PathVariable("id") long customerId, long parcelId, HttpSession session) {
		Parcel tempParcel = parcelService.findById(parcelId);
		Customer tempCustomer = customerService.findCustomerById(customerId);
		tempCustomer.getParcels().remove(tempParcel);
		customerService.update(tempCustomer);
		parcelService.deleteById(tempParcel.getParcelId());
		logger.info("Parcel collection has been confirmed by Customer");
		return "redirect:/customers/{id}";
	}

	@PostMapping("/customers/{id}/parcel{parcelId}details")
	public String parcelDetailsByCustomer(@PathVariable("id") long customerId, @PathVariable("parcelId") long parcelId,
			@RequestParam("shippingAddress") String shippingAddress, HttpSession session) {
		Parcel tempParcel = (Parcel) session.getAttribute("parcel");
		Customer tempCustomer = customerService.findCustomerById(customerId);
		tempCustomer.getParcels().remove(tempParcel);
		tempParcel.setShippingAddress(shippingAddress);
		tempCustomer.getParcels().add(tempParcel);
		customerService.update(tempCustomer);
		parcelService.update(tempParcel);
		logger.info("Parcel address has been updated");
		return "redirect:/customers/{id}";
	}

	@PostMapping("/customers/{id}/profilepicture")
	public String changeProfilePicture(@PathVariable("id") long customerId,
			@RequestParam("profilePictureURL") String profilePictureURL, HttpSession session) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		tempCustomer.setProfileUrl(profilePictureURL);
		session.setAttribute("loggedUser", tempCustomer);
		customerService.update(tempCustomer);
		logger.info("Profile picture has been updated");
		return "redirect:/customers/{id}";
	}

	@PostMapping("/customers/{id}/backgroundpicture")
	public String changeBackgroundPicture(@PathVariable("id") long customerId,
			@RequestParam("backgroundPictureURL") String backgroundPictureURL, HttpSession session) {
		Customer tempCustomer = (Customer) session.getAttribute("loggedUser");
		tempCustomer.setBackgroundUrl(backgroundPictureURL);
		session.setAttribute("loggedUser", tempCustomer);
		customerService.update(tempCustomer);
		logger.info("Background picture has been updated");
		return "redirect:/customers/{id}";
	}
}
