package com.fdmgroup.parceltracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.parceltracking.model.Parcel;
import com.fdmgroup.parceltracking.model.Status;
import com.fdmgroup.parceltracking.service.ParcelService;
import com.fdmgroup.parceltracking.service.StatusService;

@Controller
public class StatusController {

	@Autowired
	private StatusService statusService;

	@Autowired
	private ParcelService parcelService;

	@GetMapping("/statuses")
	public String statusPage() {
		return "statuscreation";
	}

	@GetMapping("/statuses/success")
	public String statusCreationSuccessPage() {
		return "statuscreationsuccess";
	}

	@GetMapping("/statuses/delete")
	public String statusDeletionPage() {
		return "statusdeletion";
	}

	@GetMapping("/statuses/delete/success")
	public String statusDeletionSuccessPage() {
		return "statusdeletionsuccess";
	}

	@PostMapping("/statuses")
	public String createStatus(@RequestParam("status") String statusName) {
		Status tempStatus = new Status(statusName);
		if (statusService.statusInDatabase(statusName)) {
			return "statuses";
		} else {
			statusService.persist(tempStatus);
			return "statuscreationsuccess";
		}
	}

	@PostMapping("/statuses/delete")
	public String deleteLocation(Model model, @RequestParam("statusName") String statusName) {
		Status tempStatus = statusService.findByStatus(statusName);

		model.addAttribute("tempLocation", tempStatus);
		if (statusService.statusInDatabase(statusName)) {
			for (Parcel p : statusService.findByStatus(statusName).getParcels()) {
				p.setStatus(null);
				parcelService.update(p);
			}
			statusService.deleteById(tempStatus.getStatusId());
			return "statusdeletionsuccess";
		} else {
			return "statusdeletion";
		}
	}

}
