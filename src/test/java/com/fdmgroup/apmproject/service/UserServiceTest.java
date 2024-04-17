package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.UserRepository;
import com.fdmgroup.apmproject.service.UserService;

@SpringBootTest
class UserServiceTest {
	
	private UserService userService;
	
	@Mock
	private UserRepository mockRepo;
	
	@BeforeEach
	void setUp() {
		userService = new UserService(mockRepo);
	}
	
	@Test
	@DisplayName("Test for persist method")
	void test1() {
		// Arrange
		User user = new User("andrew.tan",null,null,"Andrew","Tan");
		// Act
		userService.persist(user);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("andrew.tan");
		order.verify(mockRepo).save(user);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when User exists in database")
	void test2() {
		// Arrange
		User expectedUser = new User("andrew.tan",null,null,"Andrew","Tan");
		Optional <User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findByUsername(expectedUser.getUsername())).thenReturn(tempUser);
		// Act
		userService.update(expectedUser);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verify(mockRepo).save(expectedUser);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when User does not exist in database")
	void test3() {
		// Arrange
		User expectedUser = new User("andrew.tan",null,null,"Andrew","Tan");
		// Act
		userService.update(expectedUser);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(expectedUser);
	}
	
	@Test
	@DisplayName("Test for findUserById method when User exists in database")
	void test4() {
		// Arrange
		User expectedUser = new User("andrew.tan",null,null,"Andrew","Tan");
		expectedUser.setUserId(14L);
		Optional <User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findById(14L)).thenReturn(tempUser);
		// Act
		User actualUser = userService.findUserById(14L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(14L);
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);	
	}
	
	@Test
	@DisplayName("Test for findUserById method when User does not exist in database")
	void test5() {
		// Arrange
		User expectedUser = null;
		// Act
		User actualUser = userService.findUserById(2L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(2L);
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}
	
	@Test
	@DisplayName("Test for findUserByUsername method when User exists in database")
	void test6() {
		// Arrange
		User expectedUser = new User("andrew.tan",null,null,"Andrew","Tan");
		Optional <User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findByUsername(expectedUser.getUsername())).thenReturn(tempUser);
		// Act
		User actualUser = userService.findUserByUsername(expectedUser.getUsername());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername(expectedUser.getUsername());
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}
	
	@Test
	@DisplayName("Test for findUserByUsername method when User does not exist in database")
	void test7() {
		// Arrange
		User expectedUser = null;
		// Act
		User actualUser = userService.findUserByUsername("test");
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByUsername("test");
		order.verifyNoMoreInteractions();
		assertEquals(expectedUser, actualUser);
	}
	
	@Test
	@DisplayName("Test for deleteById method when User exists in database")
	void test8() {
		// Arrange
		User expectedUser = new User("andrew.tan",null,null,"Andrew","Tan");
		expectedUser.setUserId(12L);
		Optional <User> tempUser = Optional.ofNullable(expectedUser);
		when(mockRepo.findById(12L)).thenReturn(tempUser);
		// Act
		userService.deleteById(12L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(12L);
		order.verify(mockRepo).deleteById(12L);
		order.verifyNoMoreInteractions();
		
	}
	
	@Test
	@DisplayName("Test for deleteById method when User does not exist in database")
	void test9() {
		// Arrange
		User expectedUser = new User();
		expectedUser.setUserId(9L);
		// Act
		userService.deleteById(expectedUser.getUserId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(9L);
		order.verifyNoMoreInteractions();
		verify(mockRepo,never()).deleteById(9L);
	}
}
