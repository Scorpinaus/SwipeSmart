package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class UserControllerTest {

	@Autowired
    private UserController userController;

	@Autowired
    private UserService userService;

    private HttpSession session;
    private Model model;

    @BeforeEach
    public void setUp() {
    	session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    @Test
    public void testWelcomePage() {
        // Act
        String viewName = userController.welcomePage();

        // Assert
        assertEquals("index", viewName);
    }
    
    @Test
    public void testRegisterPage() {
        // Act
        String viewName = userController.registerPage();

        // Assert
        assertEquals("register", viewName);
    }
    
    @Test
    public void testLoginPage() {
        // Act
        String viewName = userController.loginPage();

        // Assert
        assertEquals("login", viewName);
    }
    
    @Test
    public void testLogoutPage() {
        // Act
        String viewName = userController.logoutPage(session);

        // Assert
        assertEquals("redirect:/login", viewName);
    }
    
    @Test
    public void testLoginPageError() {
    	// Act
        String viewName = userController.loginPageError(model);

        // Assert
        assertEquals("login", viewName);
        verify(model, times(1)).addAttribute("errorMessage", true);
    }

    @Test
    public void testDashboardPage() {
        // Arrange
    	User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = userController.dashboardPage(session, model);

        // Assert
        assertEquals("dashboard", viewName);
        verify(model).addAttribute("user", loggedUser);
    }
    
    @Test
    public void testEditProfilePage() {
        // Arrange
    	User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = userController.editProfilePage(loggedUser.getUserId(), model);

        // Assert
        assertEquals("details", viewName);
        when(model.getAttribute("user")).thenReturn(loggedUser);
    }
    
    @Test
    public void testProcessRegistration_ValidUser_RedirectToLogin() {
        // Arrange
        String username = "testuser";
        String password = "Password123";

        // Act
        String viewName = userController.processRegistration(username, password, session, model);
        User resultUser = userService.findUserByUsername("testuser");

        // Assert
        assertEquals("redirect:/login", viewName);
        assertEquals(username, resultUser.getUsername());
    }
    
    @Test
    public void testProcessRegistration_InvalidUser_RedirectToRegister() {
    	// Arrange
        String username = "testuser2";
        String password = "Passwo";

        // Act
        String viewName = userController.processRegistration(username, password, session, model);
        User resultUser = userService.findUserByUsername("testuser2");

        // Assert
        assertEquals("register", viewName);
        assertNull(resultUser);
    }
    
    @Test
    public void testProcessRegistration_InvalidAlpha_RedirectToRegister() {
    	// Arrange
        String username = "testuser2";
        String password = "Passwddo@#";

        // Act
        String viewName = userController.processRegistration(username, password, session, model);
        User resultUser = userService.findUserByUsername("testuser2");

        // Assert
        assertEquals("register", viewName);
        assertNull(resultUser);
    }
    
    

    
    

}