package com.fdmgroup.parceltracking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.parceltracking.model.Status;
import com.fdmgroup.parceltracking.repository.StatusRepository;
import com.fdmgroup.parceltracking.service.StatusService;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {
	
	private StatusService statusService;
	
	@Mock
	private StatusRepository mockRepo;
	
	@BeforeEach
	void setUp() {
		statusService = new StatusService(mockRepo);
	}
	
	@Test
	@DisplayName("Test to check that persist method saves status to database")
	void test1() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		// Act
		statusService.persist(status);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(status.getStatusId());
		order.verify(mockRepo).save(status);
		order.verifyNoMoreInteractions();
	}
		
	@Test
	@DisplayName("Test for update method when status exists in database")
	void test2() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		Optional<Status> tempStatus = Optional.ofNullable(status);
		when(mockRepo.findById(status.getStatusId())).thenReturn(tempStatus);
		// Act
		statusService.update(status);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(status.getStatusId());
		order.verify(mockRepo).save(status);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when status does not exist in database")
	void test3() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		// Act
		statusService.update(status);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(status.getStatusId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(status);
	}
	
	@Test
	@DisplayName("Test for deleteById method when status exists in database")
	void test4() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		Optional<Status> tempStatus = Optional.ofNullable(status);
		when(mockRepo.findById(status.getStatusId())).thenReturn(tempStatus);
		// Act
		statusService.deleteById(status.getStatusId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(status.getStatusId());
		order.verify(mockRepo).deleteById(status.getStatusId());
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for deleteById method when status does not exist in database")
	void test5() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		// Act
		statusService.deleteById(status.getStatusId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(status.getStatusId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(status.getStatusId());
	}
	
	@Test
	@DisplayName("Test for statusInDatabase method when status exists in database")
	void test6() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		Optional<Status> tempStatus = Optional.ofNullable(status);
		when(mockRepo.findByStatus(status.getStatusName())).thenReturn(tempStatus);
		// Act
		boolean check = statusService.statusInDatabase(status.getStatusName());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByStatus(status.getStatusName());
		order.verifyNoMoreInteractions();
		assertEquals(true, check);
	}
	
	@Test
	@DisplayName("Test for statusInDatabase method when status does not exist in database")
	void test7() {
		// Arrange
		Status status = new Status("Shipping from Origin");
		// Act
		boolean check = statusService.statusInDatabase(status.getStatusName());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByStatus(status.getStatusName());
		order.verifyNoMoreInteractions();
		assertEquals(false, check);
	}
	
	@Test
	@DisplayName("Test for findByStatus method when status exists in database")
	void test8() {
		// Arrange
		Status expectedStatus = new Status("Shipping from Origin");
		Optional<Status> tempStatus = Optional.ofNullable(expectedStatus);
		when(mockRepo.findByStatus(expectedStatus.getStatusName())).thenReturn(tempStatus);
		// Act
		Status actualStatus = statusService.findByStatus(expectedStatus.getStatusName());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByStatus(expectedStatus.getStatusName());
		order.verifyNoMoreInteractions();
		assertEquals(expectedStatus, actualStatus);
	}
	
	@Test
	@DisplayName("Test for findByStatus method when status does not exist in database")
	void test9() {
		// Arrange
		Status expectedStatus = null;
		// Act
		Status actualStatus = statusService.findByStatus("test");
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByStatus("test");
		order.verifyNoMoreInteractions();
		assertEquals(expectedStatus, actualStatus);
	}
}
