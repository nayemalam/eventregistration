package ca.mcgill.ecse321.eventregistration.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.ApplePay;
import ca.mcgill.ecse321.eventregistration.model.Circus;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Promoter;
import ca.mcgill.ecse321.eventregistration.model.Registration;

public interface ApplePayRepository extends CrudRepository<ApplePay, String> {

	ApplePay findByDeviceID(String deviceId);
	
}
