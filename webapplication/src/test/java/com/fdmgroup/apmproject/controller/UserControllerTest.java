package com.fdmgroup.apmproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

/**
 * Test suite for {@link UserController} using Spring MVC test framework.
 * This class simulates a web application context to test the interactions
 * with the {@link UserController}. It configures automatic injection of
 * mocked dependencies and a simulated MVC environment for thorough testing
 * of controller methods under various scenarios.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

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

    /**
     * Tests the behavior of accessing the welcome page.
     * <p>
     * This method verifies that when a user accesses the welcome page, the correct view name "index" is returned. It invokes the welcomePage() method of the UserController and checks whether the returned view name matches the expected value. This test ensures that the welcome page is accessible and returns the expected view name, which is crucial for the initial interaction of users with the application.
     *
     * @return The name of the view representing the welcome page.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    public void testWelcomePage() {
        // Act
        String viewName = userController.welcomePage();

        // Assert
        assertEquals("index", viewName);
    }
    
    /**
     * Tests the behavior of accessing the registration page.
     * <p>
     * This method verifies that when a user accesses the registration page, the correct view name "register" is returned. It invokes the registerPage() method of the UserController and checks whether the returned view name matches the expected value. This test ensures that the registration page is accessible and returns the expected view name, which is crucial for users to sign up for the application. By confirming the correct functioning of this page, it ensures a smooth user onboarding process.
     *
     * @return The name of the view representing the registration page.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    public void testRegisterPage() {
        // Act
        String viewName = userController.registerPage();

        // Assert
        assertEquals("register", viewName);
    }
    
    /**
     * Tests the behavior of accessing the login page.
     * <p>
     * This method verifies that when a user accesses the login page, the correct view name "login" is returned. It calls the loginPage() method of the UserController and asserts whether the returned view name matches the expected value. Ensuring the accessibility of the login page and its correct view name is essential for users to log in to the application. By confirming the correct functioning of this page, it guarantees a smooth user authentication process.
     *
     * @return The name of the view representing the login page.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    public void testLoginPage() {
        // Act
        String viewName = userController.loginPage();

        // Assert
        assertEquals("login", viewName);
    }
    
    /**
     * Tests the behavior of logging out a user.
     * <p>
     * This method verifies that when a user logs out, the correct redirection to the login page occurs. It calls the logoutPage() method of the UserController with a session parameter and asserts whether the returned view name corresponds to the expected redirection to the login page. Ensuring proper logout functionality is crucial for maintaining application security and user privacy. By testing this functionality, it ensures that users can securely log out of their accounts, preventing unauthorized access to sensitive information.
     *
     * @param session The session of the logged-in user.
     * @return The name of the view representing the redirection to the login page.
     * @throws AssertionError If the test fails to assert the expected behavior.
     * @see org.junit.jupiter.api.Test
     */
    @Test
    public void testLogoutPage() {
        // Act
        String viewName = userController.logoutPage(session);

        // Assert
        assertEquals("redirect:/login", viewName);
    }
    
    /**
     * Tests the behavior of the UserController's loginPageError method when an error occurs during login.
     * Adds an error message attribute to the model and verifies that the view name is "login".
     *
     * @param model The model used to add attributes for the view.
     * @return The view name "login".
     */
    @Test
    public void testLoginPageError() {
    	// Act
        String viewName = userController.loginPageError(model);

        // Assert
        assertEquals("login", viewName);
        verify(model, times(1)).addAttribute("errorMessage", true);
    }

    /**
     * Tests the UserController's dashboardPage method, which retrieves the dashboard view.
     * Adds the logged-in user as an attribute to the model and verifies that the view name is "dashboard".
     *
     * @param session The session object to retrieve the logged-in user.
     * @param model   The model used to add attributes for the view.
     * @return The view name "dashboard".
     * @throws ExceptionType Throws if an error occurs during user retrieval.
     * @see UserController#dashboardPage(HttpSession, Model)
     */
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
    
    /**
     * Tests the UserController's editProfilePage method, which retrieves the profile edit view.
     * Adds the logged-in user as an attribute to the model and verifies that the view name is "details".
     *
     * @param userId The ID of the user whose profile is being edited.
     * @param model  The model used to add attributes for the view.
     * @return The view name "details".
     * @throws ExceptionType Throws if an error occurs during user retrieval.
     * @see UserController#editProfilePage(Long, Model)
     */
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
    
    /**
     * Tests the UserController's processRegistration method with valid user credentials.
     * Registers a new user with the provided username and password, then verifies redirection to the login page.
     *
     * @param username The username of the user to be registered.
     * @param password The password of the user to be registered.
     * @param session  The session object used for user authentication.
     * @param model    The model used to add attributes for the view.
     * @return The view name "redirect:/login".
     * @throws ExceptionType Throws if an error occurs during user registration or retrieval.
     * @see UserController#processRegistration(String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
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
    
    /**
     * Tests the UserController's processRegistration method with invalid user credentials.
     * Attempts to register a new user with an invalid password, then verifies redirection to the registration page.
     *
     * @param username The username of the user to be registered.
     * @param password The password of the user to be registered (invalid format).
     * @param session  The session object used for user authentication.
     * @param model    The model used to add attributes for the view.
     * @return The view name "register".
     * @throws ExceptionType Throws if an error occurs during user registration or retrieval.
     * @see UserController#processRegistration(String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
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
    
    /**
     * Tests the UserController's processRegistration method with invalid user credentials.
     * Attempts to register a new user with an invalid password, then verifies redirection to the registration page.
     *
     * @param username The username of the user to be registered.
     * @param password The password of the user to be registered (invalid format).
     * @param session  The session object used for user authentication.
     * @param model    The model used to add attributes for the view.
     * @return The view name "register".
     * @throws ExceptionType Throws if an error occurs during user registration or retrieval.
     * @see UserController#processRegistration(String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
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
    
    /**
     * Tests the UserController's editCustomerProfile method to update the user's address.
     * Updates the user's address and verifies that the changes are reflected in the database.
     *
     * @param address   The new address to be updated.
     * @param loggedUser The user whose profile is being updated.
     * @param session   The session object used for user authentication.
     * @param model     The model used to add attributes for the view.
     * @return The view name "profile".
     * @throws ExceptionType Throws if an error occurs during profile update or user retrieval.
     * @see UserController#editCustomerProfile(long, String, String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
    @Test
    public void testEditCustomerProfile_UpdateAddress() {
        // Arrange
        String address = "New Address";
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        // Act
        String viewName = userController.editCustomerProfile(loggedUser.getUserId(), address, loggedUser.getFirstName(), loggedUser.getLastName(), session, model);
        User updatedUser = userService.findUserByUsername("jackytan");

        // Assert
        assertEquals("profile", viewName);
        assertEquals(address, updatedUser.getAddress());
    }
    
    /**
     * Tests the UserController's editCustomerProfile method to update the user's first name.
     * Updates the user's first name and verifies that the changes are reflected in the database.
     *
     * @param firstName The new first name to be updated.
     * @param loggedUser The user whose profile is being updated.
     * @param session   The session object used for user authentication.
     * @param model     The model used to add attributes for the view.
     * @return The view name "profile".
     * @throws ExceptionType Throws if an error occurs during profile update or user retrieval.
     * @see UserController#editCustomerProfile(long, String, String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
    @Test
    public void testEditCustomerProfile_UpdateFirstName() {
        // Arrange
        String firstName = "New First Name";
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        
        // Act
        String viewName = userController.editCustomerProfile(loggedUser.getUserId(), loggedUser.getAddress(), firstName, loggedUser.getLastName(), session, model);
        User updatedUser = userService.findUserByUsername("jackytan");

        // Assert
        assertEquals("profile", viewName);
        assertEquals(firstName, updatedUser.getFirstName());
    }

    /**
     * Tests the UserController's editCustomerProfile method to update the user's last name.
     * Updates the user's last name and verifies that the changes are reflected in the database.
     *
     * @param lastName  The new last name to be updated.
     * @param loggedUser    The user whose profile is being updated.
     * @param session   The session object used for user authentication.
     * @param model     The model used to add attributes for the view.
     * @return The view name "profile".
     * @throws ExceptionType    Throws if an error occurs during profile update or user retrieval.
     * @see UserController#editCustomerProfile(long, String, String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
    @Test
    public void testEditCustomerProfile_UpdateLastName() {
        String lastName = "New Last Name";
        User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = userController.editCustomerProfile(loggedUser.getUserId(), loggedUser.getAddress(), loggedUser.getFirstName(), lastName, session, model);
        User updatedUser = userService.findUserByUsername("jackytan");

        // Assert
        assertEquals("profile", viewName);
        assertEquals(lastName, updatedUser.getLastName());
    }

    /**
     * Tests the UserController's editCustomerProfile method when no updates are made to the user's profile.
     * Verifies that the profile remains unchanged after the method call.
     *
     * @param loggedUser    The user whose profile is being edited.
     * @param session   The session object used for user authentication.
     * @param model     The model used to add attributes for the view.
     * @return The view name "profile".
     * @throws ExceptionType    Throws if an error occurs during profile retrieval.
     * @see UserController#editCustomerProfile(long, String, String, String, HttpSession, Model)
     * @see UserService#findUserByUsername(String)
     */
    @Test
    public void testEditCustomerProfile_NoUpdates() {
        // Arrange
    	User loggedUser = userService.findUserByUsername("jackytan"); 
        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

        // Act
        String viewName = userController.editCustomerProfile(loggedUser.getUserId(), "", "", "", session, model);
        User updatedUser = userService.findUserByUsername("jackytan");
        
        // Assert
        assertEquals("profile", viewName);
        when(model.getAttribute("user")).thenReturn(updatedUser);
    }
    
    

}