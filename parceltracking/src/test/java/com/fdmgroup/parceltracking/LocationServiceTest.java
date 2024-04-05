package com.fdmgroup.parceltracking;

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

import com.fdmgroup.parceltracking.model.Location;
import com.fdmgroup.parceltracking.repository.LocationRepository;
import com.fdmgroup.parceltracking.service.LocationService;

@SpringBootTest
class LocationServiceTest {
	
	private LocationService locationService;
	
	@Mock
	private LocationRepository mockRepo;
	
	@BeforeEach
	void setUp() {
		locationService = new LocationService(mockRepo);
	}
	
	@Test
	@DisplayName("Test to check that persist method saves location to database")
	void test1() {
		// Arrange
		Location location = new Location("Singapore","Singapore");
		// Act
		locationService.persist(location);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(location.getLocationId());
		order.verify(mockRepo).save(location);
		order.verifyNoMoreInteractions();
	}
		
	@Test
	@DisplayName("Test for update method when location exists in database")
	void test2() {
		// Arrange
		Location location = new Location("Singapore", "Singapore");
		Optional<Location> tempLocation = Optional.ofNullable(location);
		when(mockRepo.findById(location.getLocationId())).thenReturn(tempLocation);
		// Act
		locationService.update(location);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(location.getLocationId());
		order.verify(mockRepo).save(location);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when location does not exist in database")
	void test3() {
		// Arrange
		Location location = new Location("Singapore", "Singapore");
		// Act
		locationService.update(location);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(location.getLocationId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(location);
	}
	
	@Test
	@DisplayName("Test for deleteById method when location exists in database")
	void test4() {
		// Arrange
		Location location = new Location("Singapore", "Singapore");
		Optional<Location> tempLocation = Optional.ofNullable(location);
		when(mockRepo.findById(location.getLocationId())).thenReturn(tempLocation);
		// Act
		locationService.deleteById(location.getLocationId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(location.getLocationId());
		order.verify(mockRepo).deleteById(location.getLocationId());
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for deleteById method when location does not exist in database")
	void test5() {
		// Arrange
		Location location = new Location("Singapore", "Singapore");
		// Act
		locationService.deleteById(location.getLocationId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(location.getLocationId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(location.getLocationId());
	}
}
