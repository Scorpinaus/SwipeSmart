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

/**
 * Tests the functionality of the StatusService class. Uses Mockito to mock the
 * StatusRepository and Logger dependencies, allowing isolated testing of the
 * StatusService methods.
 *
 * @see StatusService The class under test.
 * @see StatusRepository The repository interface for status-related database
 *      operations.
 * @see Logger The logger utility used for logging.
 */

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

	@Mock
	private StatusRepository statusRepo;

	@Mock
	private Logger logger;

	@InjectMocks
	private StatusService statusService;

	private Status status;

	/**
	 * Sets up the test environment before each test case.
	 */
	@BeforeEach
	public void setUp() {
		status = new Status();
		status.setStatusId(1);

	}

	/**
	 * Tests the creation of a status when it is not present in the database.
	 * Verifies that the StatusService persists the new status by saving it to the
	 * repository.
	 *
	 * @param newStatus The status object to be created.
	 * @return The newly created status object.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#persist(Status) The method responsible for persisting the
	 *      status.
	 * @see StatusRepository#save(Object) The repository method used to save the
	 *      status.
	 */
	@Test
	@DisplayName("Test for status creation when not present")
	void testCreateOne() {
		// Arrange
		Status newStatus = status;
		when(statusRepo.findById(newStatus.getStatusId())).thenReturn(Optional.empty());

		// Act
		statusService.persist(newStatus);

		// Assert
		verify(statusRepo).save(newStatus);
	}

	/**
	 * Tests the creation of a status when the status is already present in the
	 * database. Verifies that the StatusService does not persist the status by
	 * saving it to the repository again.
	 *
	 * @param statusOne The existing status object to be tested for creation.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#persist(Status) The method responsible for persisting the
	 *      status.
	 * @see StatusRepository#save(Object) The repository method used to save the
	 *      status.
	 */
	@Test
	@DisplayName("Test for status creation when status is present")
	void testCreateTwo() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));

		// Act
		statusService.persist(statusOne);

		// Assert
		verify(statusRepo, never()).save(statusOne);
	}

	/**
	 * Tests the update of a status when the status is not present in the database.
	 * Verifies that the StatusService does not attempt to update the status by
	 * saving it to the repository.
	 *
	 * @param statusOne The status object to be tested for update.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#update(Status) The method responsible for updating the
	 *      status.
	 * @see StatusRepository#save(Object) The repository method used to save the
	 *      status.
	 */
	@Test
	@DisplayName("Test for status update when status is not present")
	void testUpdateOne() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());

		// Act
		statusService.update(statusOne);

		// Assert
		verify(statusRepo, never()).save(statusOne);
	}

	/**
	 * Tests the update of a status when the status already exists in the database.
	 * Verifies that the StatusService updates the status by saving it to the
	 * repository.
	 *
	 * @param statusOne The status object to be tested for update.
	 * @return Description of the method's behavior.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#update(Status) The method responsible for updating the
	 *      status.
	 * @see StatusRepository#save(Object) The repository method used to save the
	 *      status.
	 */
	@Test
	@DisplayName("Test for status update when status already exists")
	void testUpdateTwo() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));

		// Act
		statusService.update(statusOne);

		// Assert
		verify(statusRepo).save(statusOne);
	}

	/**
	 * Tests the findById method of StatusService when searching for a non-existing
	 * status. Verifies that the method returns null when the status is not found in
	 * the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @return Null if the status is not found in the repository.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#findById(int) The method under test for retrieving a
	 *      status by ID.
	 * @see StatusRepository#findById(Object) The repository method used for finding
	 *      a status by ID.
	 */
	@Test
	@DisplayName("Test for findById for non-existing status")
	void testFindByIdOne() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());

		// Act
		Status result = statusService.findById(statusOne.getStatusId());

		// Assert
		assertNull(result, "Result should be null");
		verify(statusRepo).findById(statusOne.getStatusId());
	}

	/**
	 * Tests the findById method of StatusService when searching for an existing
	 * status. Verifies that the method returns the correct status object when the
	 * status is found in the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @return The status object found in the repository.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#findById(int) The method under test for retrieving a
	 *      status by ID.
	 * @see StatusRepository#findById(Object) The repository method used for finding
	 *      a status by ID.
	 */
	@Test
	@DisplayName("Test for findById for existing status")
	void testFindByIdTwo() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));

		// Act
		Status result = statusService.findById(statusOne.getStatusId());

		// Assert
		assertNotNull(result, "Result should not be null");
		verify(statusRepo).findById(statusOne.getStatusId());
	}

	/**
	 * Tests the findByStatusName method of StatusService when searching for a
	 * non-existing status. Verifies that the method returns null when the status
	 * with the given name is not found in the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @return The null result indicating that the status with the given name was
	 *         not found.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#findByStatusName(String) The method under test for
	 *      retrieving a status by name.
	 * @see StatusRepository#findByStatusName(String) The repository method used for
	 *      finding a status by name.
	 */
	@Test
	@DisplayName("Test for findByStatusName for non-existing status")
	void testFindByStatusNameOne() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findByStatusName(statusOne.getStatusName())).thenReturn(Optional.empty());

		// Act
		Status result = statusService.findByStatusName(statusOne.getStatusName());

		// Assert
		assertNull(result, "Result should be null");
		verify(statusRepo).findByStatusName(statusOne.getStatusName());
	}

	/**
	 * Tests the findByStatusName method of StatusService when searching for an
	 * existing status. Verifies that the method returns a non-null result when the
	 * status with the given name is found in the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @return The non-null result indicating that the status with the given name
	 *         was found.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#findByStatusName(String) The method under test for
	 *      retrieving a status by name.
	 * @see StatusRepository#findByStatusName(String) The repository method used for
	 *      finding a status by name.
	 */
	@Test
	@DisplayName("Test for findByStatusName for existing status")
	void testFindByStatusNameTwo() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findByStatusName(statusOne.getStatusName())).thenReturn(Optional.of(statusOne));

		// Act
		Status result = statusService.findByStatusName(statusOne.getStatusName());

		// Assert
		assertNotNull(result, "Result should be not null");
		verify(statusRepo).findByStatusName(statusOne.getStatusName());
	}

	/**
	 * Tests the findByStatusName method of StatusService when searching for an
	 * existing status. Verifies that the method returns a non-null result when the
	 * status with the given name is found in the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @return The non-null result indicating that the status with the given name
	 *         was found.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#findByStatusName(String) The method under test for
	 *      retrieving a status by name.
	 * @see StatusRepository#findByStatusName(String) The repository method used for
	 *      finding a status by name.
	 */
	@Test
	@DisplayName("Test for deleteById for non-existing status")
	void testDeleteByIdOne() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.empty());

		// Act
		statusService.deleteById(statusOne.getStatusId());

		// Assert
		verify(statusRepo).findById(statusOne.getStatusId());
		verify(statusRepo, never()).deleteById(statusOne.getStatusId());
	}

	/**
	 * Tests the deleteById method of StatusService when deleting an existing
	 * status. Verifies that the method successfully deletes the status with the
	 * given ID from the repository.
	 *
	 * @param statusOne The status object used for testing.
	 * @throws ExceptionType If an unexpected exception occurs during the test.
	 * @see StatusService#deleteById(int) The method under test for deleting a
	 *      status by ID.
	 * @see StatusRepository#findById(int) The repository method used for finding a
	 *      status by ID.
	 * @see StatusRepository#deleteById(int) The repository method used for deleting
	 *      a status by ID.
	 */
	@Test
	@DisplayName("Test for deleteById for existing status")
	void testDeleteByIdTwo() {
		// Arrange
		Status statusOne = status;
		when(statusRepo.findById(statusOne.getStatusId())).thenReturn(Optional.of(statusOne));

		// Act
		statusService.deleteById(statusOne.getStatusId());

		// Assert
		verify(statusRepo).findById(statusOne.getStatusId());
		verify(statusRepo).deleteById(statusOne.getStatusId());
	}

}
