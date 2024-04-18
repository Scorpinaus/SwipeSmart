package com.fdmgroup.apmproject.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationSuccessHandlerSecurity implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private static Logger logger = LogManager.getLogger(AuthenticationSuccessHandlerSecurity.class);

	@Autowired
	HttpSession session;

	@Autowired
	UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserSecurityDetails loggedUser = (UserSecurityDetails) authentication.getPrincipal();
		session.setAttribute("loggedUser", loggedUser.getUser());
		logger.info("User has logged in");
		if (loggedUser.getUser().getRole().equals("ROLE_ADMIN")) {
			redirectStrategy.sendRedirect(request, response, "/admin/dashboard");
		} else {
			redirectStrategy.sendRedirect(request, response, "/dashboard");
		}

		;
	}

}
