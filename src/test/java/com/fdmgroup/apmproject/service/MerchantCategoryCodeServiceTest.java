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

import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;

@SpringBootTest
class MerchantCategoryCodeServiceTest {
	
	private MerchantCategoryCodeService merchantCategoryCodeService;
	
	@Mock
	private MerchantCategoryCodeRepository mockRepo;
	
	@BeforeEach
	void setUp() {
		merchantCategoryCodeService = new MerchantCategoryCodeService(mockRepo);
	}
	
	@Test
	@DisplayName("Test for persist method")
	void test1() {
		// Arrange
		MerchantCategoryCode merchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		merchantCategoryCode.setMerchantCategoryCodeId(12);
		// Act
		merchantCategoryCodeService.persist(merchantCategoryCode);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(merchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).save(merchantCategoryCode);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when MerchantCategoryCode exists in database")
	void test2() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional <MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId())).thenReturn(tempMerchantCategoryCode);
		// Act
		merchantCategoryCodeService.update(expectedMerchantCategoryCode);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).save(expectedMerchantCategoryCode);
		order.verifyNoMoreInteractions();
	}
	
	@Test
	@DisplayName("Test for update method when MerchantCategoryCode does not exist in database")
	void test3() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		// Act
		merchantCategoryCodeService.update(expectedMerchantCategoryCode);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		verify(mockRepo, never()).save(expectedMerchantCategoryCode);
	}
	
	@Test
	@DisplayName("Test for findById method when MerchantCategoryCode exists in database")
	void test4() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional <MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId())).thenReturn(tempMerchantCategoryCode);
		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);	
	}
	
	@Test
	@DisplayName("Test for findById method when MerchantCategoryCode does not exist in database")
	void test5() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = null;
		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findById(2);
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(2);
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}
	
	@Test
	@DisplayName("Test for findByMerchantCategory method when MerchantCategoryCode exists in database")
	void test6() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		Optional <MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory())).thenReturn(tempMerchantCategoryCode);
		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByMerchantCategory(expectedMerchantCategoryCode.getMerchantCategory());
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}
	
	@Test
	@DisplayName("Test for findByMerchantCategory method when MerchantCategoryCode does not exist in database")
	void test7() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = null;
		// Act
		MerchantCategoryCode actualMerchantCategoryCode = merchantCategoryCodeService.findByMerchantCategory("test");
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findByMerchantCategory("test");
		order.verifyNoMoreInteractions();
		assertEquals(expectedMerchantCategoryCode, actualMerchantCategoryCode);
	}
	
	@Test
	@DisplayName("Test for deleteById method when MerchantCategoryCode exists in database")
	void test8() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode(12,"Trial Category");
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(12);
		Optional <MerchantCategoryCode> tempMerchantCategoryCode = Optional.ofNullable(expectedMerchantCategoryCode);
		when(mockRepo.findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId())).thenReturn(tempMerchantCategoryCode);
		// Act
		merchantCategoryCodeService.deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verify(mockRepo).deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		
	}
	
	@Test
	@DisplayName("Test for deleteById method when MerchantCategoryCode does not exist in database")
	void test9() {
		// Arrange
		MerchantCategoryCode expectedMerchantCategoryCode = new MerchantCategoryCode();
		expectedMerchantCategoryCode.setMerchantCategoryCodeId(9);
		// Act
		merchantCategoryCodeService.deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		// Assert
		InOrder order = inOrder(mockRepo);
		order.verify(mockRepo).findById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
		order.verifyNoMoreInteractions();
		verify(mockRepo,never()).deleteById(expectedMerchantCategoryCode.getMerchantCategoryCodeId());
	}

}
