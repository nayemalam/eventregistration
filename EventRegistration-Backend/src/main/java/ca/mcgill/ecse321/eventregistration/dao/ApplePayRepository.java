package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.ApplePay;

public interface ApplePayRepository extends CrudRepository<ApplePay, String> {

	ApplePay findByDeviceID(String deviceId);
	
}
