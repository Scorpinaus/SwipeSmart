package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;

import jakarta.annotation.PostConstruct;

/**
 * This class is responsible for handling all business logic related to MerchantCategoryCode.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Service
public class MerchantCategoryCodeService {
	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepo;
	
	private static Logger logger = LogManager.getLogger(MerchantCategoryCodeService.class);
	
	public MerchantCategoryCodeService(MerchantCategoryCodeRepository rewardRepo) {
		this.merchantCategoryCodeRepo = rewardRepo;
	}
	
	/**
	 * Registers a new Merchant Category Code (MCC) or logs a warning if it already exists.
	 * <p>
	 * This method checks the repository to see if an MCC with the same identifier already exists. If the MCC does not exist, it is saved to the repository, and an informational message is logged indicating successful creation. If the MCC is already registered, a warning message is logged, and no further action is taken. This method ensures that MCC data is not duplicated in the database.
	 *
	 * @param merchantCategoryCode The MerchantCategoryCode object to be registered or checked.
	 * @return void This method does not return a value but logs the outcome of the registration attempt.
	 * @throws DataAccessException If there are issues accessing the database or during the save operation.
	 * @see MerchantCategoryCodeRepo#findById(Object) Method to check if an MCC already exists based on the identifier.
	 * @see MerchantCategoryCodeRepo#save(Object) Method to save a new MCC into the database.
	 */
	public void persist(MerchantCategoryCode merchantCategoryCode) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(merchantCategoryCode.getMerchantCategoryCodeId());
		if (returnedMerchantCategoryCode.isEmpty()) {
			merchantCategoryCodeRepo.save(merchantCategoryCode);
			logger.info("MCC successfully created");
		} else {
			logger.warn("MCC already exists");
		}
	}
	
	/**
	 * Updates an existing Merchant Category Code (MCC) in the database.
	 * <p>
	 * This method first checks if an MCC with the specified identifier exists in the repository. If the MCC is not found, a warning is logged indicating that it does not exist. If the MCC exists, it is updated with the provided details, and a confirmation message is logged. This ensures that only existing MCCs are updated, preventing the inadvertent creation of duplicate entries.
	 *
	 * @param merchantCategoryCode The MerchantCategoryCode object containing updated information to be saved.
	 * @return void This method does not return a value, but logs the outcome of the update operation.
	 * @throws DataAccessException If there are issues accessing the database or saving the updated MCC data.
	 * @see MerchantCategoryCodeRepo#findById(Object) Method to verify the existence of the MCC based on its identifier.
	 * @see MerchantCategoryCodeRepo#save(Object) Method to save the updated MCC data into the database.
	 */
	public void update(MerchantCategoryCode merchantCategoryCode) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(merchantCategoryCode.getMerchantCategoryCodeId());
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("MCC does not exist in database");
		} else {
			merchantCategoryCodeRepo.save(merchantCategoryCode);
			logger.info("MCC successfully updated");
		}
	}
	
	/**
	 * Retrieves a Merchant Category Code (MCC) by its unique identifier.
	 * <p>
	 * This method searches the database for an MCC using the provided identifier. If the MCC is found, it is returned along with an informational log message. If no MCC is found with the given identifier, a warning is logged and the method returns null. This method is commonly used for verification and retrieval of specific MCC details within the system.
	 *
	 * @param id The unique identifier for the MCC to be retrieved.
	 * @return MerchantCategoryCode object if found; null if no MCC matches the given ID.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the MCC data.
	 * @see MerchantCategoryCodeRepo#findById(int) Method used to locate the MCC by its identifier.
	 */
	public MerchantCategoryCode findById(int id) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(id);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Could not find MCC in Database");
			return null;
		} else {
			logger.info("Returning MCC details");
			return returnedMerchantCategoryCode.get();
		}
	}
	
	/**
	 * Retrieves a Merchant Category Code (MCC) based on the merchant category description.
	 * <p>
	 * This method searches the database for an MCC that matches the provided category description. If an MCC with the specified description is found, it is returned along with an informational log message. If no such MCC exists, a warning is logged and the method returns null. This method is useful for identifying MCC details when only the merchant category description is known.
	 *
	 * @param MerchantCategory The description of the merchant category used to find the corresponding MCC.
	 * @return MerchantCategoryCode object if an MCC with the specified description is found; null if no such MCC exists.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the MCC data.
	 * @see MerchantCategoryCodeRepo#findByMerchantCategory(String) Method used to locate the MCC by its merchant category description.
	 */
	public MerchantCategoryCode findByMerchantCategory(String MerchantCategory) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findByMerchantCategory(MerchantCategory);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Could not find MCC in Database");
			return null;
		} else {
			logger.info("Returning MCC details");
			return returnedMerchantCategoryCode.get();
		}
	}
	
	/**
	 * Deletes a Merchant Category Code (MCC) from the database by its unique identifier.
	 * <p>
	 * This method first checks if an MCC with the specified identifier exists in the repository. If the MCC is not found, a warning is logged indicating its absence. If the MCC exists, it is removed from the database and a confirmation message is logged. This method ensures that MCC data is precisely managed by preventing the deletion of non-existent entries.
	 *
	 * @param id The unique identifier of the MCC to be deleted.
	 * @return void This method does not return a value, but logs the outcome of the deletion process.
	 * @throws DataAccessException If there are issues accessing the database or executing the delete operation.
	 * @see MerchantCategoryCodeRepo#findById(int) Method to verify the existence of the MCC based on its identifier.
	 * @see MerchantCategoryCodeRepo#deleteById(int) Method to remove the MCC from the database.
	 */
	public void deleteById(int id) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(id);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("MCC does not exist in database");
		} else {
			merchantCategoryCodeRepo.deleteById(id);
			logger.info("MCC deleted from Database");
		}
	}
}
