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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.parceltracking.model.Parcel;
import com.fdmgroup.parceltracking.repository.ParcelRepository;
import com.fdmgroup.parceltracking.service.ParcelService;

@SpringBootTest
class ParcelServiceTest {
	
	private ParcelService parcelService;
	
	@Mock
	private ParcelRepository mockRepo;
	
	@BeforeEach
	void setUp() {
		parcelService = new ParcelService(mockRepo);
	}
	
	@Test
	@DisplayName("Test to check that persist method saves parcel to database")
	void test1() {
		// Arrange
		Parcel parcel = new Parcel();
		// Act
		parcelService.persist(parcel);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(parcel.getParcelId());
		order.verify(mockRepo).save(parcel);
		order.verifyNoMoreInteractions();
	}
		
	@Test
	@DisplayName("Test for update method when parcel exists in database")
	void test2() {
		// Arrange
		Parcel parcel = new Parcel();
		Optional<Parcel> tempParcel = Optional.ofNullable(parcel);
		when(mockRepo.findById(parcel.getParcelId())).thenReturn(tempParcel);
		// Act
		parcelService.update(parcel);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(parcel.getParcelId());
		order.verify(mockRepo).save(parcel);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when parcel does not exist in database")
	void test3() {
		// Arrange
		Parcel parcel = new Parcel();
		// Act
		parcelService.update(parcel);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(parcel.getParcelId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(parcel);
	}
	
	@Test
	@DisplayName("Test for deleteById method when parcel exists in database")
	void test4() {
		// Arrange
		Parcel parcel = new Parcel();
		Optional<Parcel> tempParcel = Optional.ofNullable(parcel);
		when(mockRepo.findById(parcel.getParcelId())).thenReturn(tempParcel);
		// Act
		parcelService.deleteById(parcel.getParcelId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(parcel.getParcelId());
		order.verify(mockRepo).deleteById(parcel.getParcelId());
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for deleteById method when parcel does not exist in database")
	void test5() {
		// Arrange
		Parcel parcel = new Parcel();
		// Act
		parcelService.deleteById(parcel.getParcelId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(parcel.getParcelId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).deleteById(parcel.getParcelId());
	}
	
	@Test
	@DisplayName("Test for findById method when parcel exists in database")
	void test6() {
		// Arrange
		Parcel expectedParcel = new Parcel();
		Optional<Parcel> tempParcel = Optional.ofNullable(expectedParcel);
		when(mockRepo.findById(expectedParcel.getParcelId())).thenReturn(tempParcel);
		// Act
		Parcel actualParcel = parcelService.findById(expectedParcel.getParcelId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedParcel.getParcelId());
		order.verifyNoMoreInteractions();
		assertEquals(expectedParcel, actualParcel);
	}
	
	@Test
	@DisplayName("Test for findById method when parcel does not exist in database")
	void test7() {
		// Arrange
		Parcel expectedParcel = null;
		// Act
		Parcel actualParcel = parcelService.findById(10L);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(10L);
		order.verifyNoMoreInteractions();
		assertEquals(expectedParcel, actualParcel);
	}	
}
