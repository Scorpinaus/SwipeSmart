package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.repository.StatusRepository;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {
	
	@Mock
	private StatusRepository statusRepo;
	
	@Mock private Logger logger;
	
	@InjectMocks
	private StatusService statusService;
	
	private Status status;
	
	@BeforeEach
	public void setUp() {
		status = new Status();
		status.setStatusId(1);
		
	}
	
	@Test
	@DisplayName("Test for status creation when not present")
	void testCreateOne() {
		//Arrange
		Status newStatus = status;
		when (statusRepo.findById(newStatus.getStatusId())).thenReturn(Optional.empty());
		
		//Act
		statusService.persist(newStatus);
		
		//Assert
		verify(statusRepo).save(newStatus);
	}
	
	@Test
	@DisplayName("Test for status creation when status is present")
	void testCreateTwo() {
		//Arrange
		Status statusOne = status;
		when (statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));
		
		//Act
		statusService.persist(statusOne);
		
		//Assert
		verify(statusRepo, never()).save(statusOne);
	}
	
	@Test
	@DisplayName("Test for status update when status is not present")
	void testUpdateOne() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());
		
		//Act
		statusService.update(statusOne);
		
		//Assert
		verify(statusRepo, never()).save(statusOne);
	}
	
	@Test
	@DisplayName("Test for status update when status already exists")
	void testUpdateTwo() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));
		
		//Act
		statusService.update(statusOne);
		
		//Assert
		verify(statusRepo).save(statusOne);
	}
	
	@Test
	@DisplayName("Test for findById for non-existing status")
	void testFindByIdOne() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());
		
		//Act
		Status result = statusService.findById(statusOne.getStatusId());
		
		//Assert
		assertNull(result, "Result should be null");
		verify(statusRepo).findById(statusOne.getStatusId());
	}
	
	@Test
	@DisplayName("Test for findById for existing status")
	void testFindByIdTwo() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));
		
		//Act
		Status result = statusService.findById(statusOne.getStatusId());
		
		//Assert
		assertNotNull(result, "Result should not be null");
		verify(statusRepo).findById(statusOne.getStatusId());
	}
	
	@Test
	@DisplayName("Test for findByStatusName for non-existing status")
	void testFindByStatusNameOne() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findByStatusName(statusOne.getStatusName())).thenReturn(Optional.empty());
		
		//Act
		Status result = statusService.findByStatusName(statusOne.getStatusName());
		
		//Assert
		assertNull(result, "Result should be null");
		verify(statusRepo).findByStatusName(statusOne.getStatusName());
	}
	
	@Test
	@DisplayName("Test for findByStatusName for existing status")
	void testFindByStatusNameTwo() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findByStatusName(statusOne.getStatusName())).thenReturn(Optional.of(statusOne));
		
		//Act
		Status result = statusService.findByStatusName(statusOne.getStatusName());
		
		//Assert
		assertNotNull(result, "Result should be not null");
		verify(statusRepo).findByStatusName(statusOne.getStatusName());
	}
	
	@Test
	@DisplayName("Test for deleteById for non-existing status")
	void testDeleteByIdOne() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());
		
		//Act
		statusService.deleteById(statusOne.getStatusId());
		
		//Assert
		verify(statusRepo).findById(statusOne.getStatusId());
		verify(statusRepo, never()).deleteById(statusOne.getStatusId());
	}
	
	@Test
	@DisplayName("Test for deleteById for existing status")
	void testDeleteByIdTwo() {
		//Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));
		
		//Act
		statusService.deleteById(statusOne.getStatusId());
		
		//Assert
		verify(statusRepo).findById(statusOne.getStatusId());
		verify(statusRepo).deleteById(statusOne.getStatusId());
	}


}
