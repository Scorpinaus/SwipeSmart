package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock
	private TransactionRepository transactionRepo;
	
	@Mock
	private Logger logger;
	
	@InjectMocks
	private TransactionService transactionService;
	
	private Transaction transactions;
	
	
	@BeforeEach
	private void setUp() {
		transactions = new Transaction();
		transactions.setTransactionId(1L);
	}
	
	@Test
	@DisplayName("Test Persist function for new Transaction")
	public void testPersistOne() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());
		
		//Act
		transactionService.persist(transactionOne);
		
		//Assert
		verify(transactionRepo).save(transactionOne);
	}
	
	@Test
	@DisplayName("Test Persist function for existing Transaction")
	public void testPersistTwo() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));
		
		//Act
		transactionService.persist(transactionOne);
		
		//Assert
		verify(transactionRepo, never()).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test update function for non-existing Transaction")
	public void testUpdateOne() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());
		
		//Act
		transactionService.update(transactionOne);
		
		//Assert
		verify(transactionRepo, never()).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test update function for existing Transaction")
	public void testUpdateTwo() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));
		
		//Act
		transactionService.update(transactionOne);
		
		//Assert
		verify(transactionRepo).save(transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test findById function for non-existing Transaction")
	public void testFindByIdOne() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());
		
		//Act
		Transaction result = transactionService.findById(transactionOne.getTransactionId());
		
		//Assert
		assertNull(result);
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test findById function for existing Transaction")
	public void testFindByIdTwo() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));
		
		//Act
		Transaction result = transactionService.findById(transactionOne.getTransactionId());
		
		//Assert
		assertNotNull(result);
		assertEquals(result, transactionOne);
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test deleteById function for non-existing Transaction")
	public void testDeleteByIdOne() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.empty());
		
		//Act
		transactionService.deleteById(transactionOne.getTransactionId());
		
		//Assert
		verify(transactionRepo, never()).deleteById(transactionOne.getTransactionId());
		verifyNoMoreInteractions(transactionRepo);
	}
	
	@Test
	@DisplayName("Test deleteById function for existing Transaction")
	public void testDeleteByIdTwo() {
		//Arrange
		Transaction transactionOne = transactions;
		when(transactionRepo.findById(transactionOne.getTransactionId())).thenReturn(Optional.of(transactionOne));
		
		//Act
		transactionService.deleteById(transactionOne.getTransactionId());
		
		//Assert
		verify(transactionRepo).deleteById(transactionOne.getTransactionId());
		verifyNoMoreInteractions(transactionRepo);
	}
	
}
