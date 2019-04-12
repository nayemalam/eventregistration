package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private PromoterRepository promoterRepository;
	@Autowired
	private CircusRepository circusRepository;
	@Autowired
	private ApplePayRepository applePayRepository;
	
	
	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}


	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}
	
	// start implementation of service methods
	
	/*
	 * ============================
	 * PROMOTER SERVICE METHODS
	 * ============================
	 */	
	
	// TO DO: JAVADOC
	@Transactional
	public Promoter createPromoter (String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Promoter name cannot be empty!");
		} else if (promoterRepository.existsById(name)) {
			throw new IllegalArgumentException("Promoter has already been created!");
		}
		Promoter promoter = new Promoter();
		promoter.setName(name);
		promoterRepository.save(promoter);
		return promoter;
	}
	
	// TO DO: JAVADOC
	@Transactional
	public List<Promoter> getAllPromoters () {
		return toList(promoterRepository.findAll());
	}
	
	// TO DO: JAVADOC
	@Transactional
	public Promoter getPromoter (String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		
		Promoter promoter = promoterRepository.findByName(name);
		
		return promoter;
	}
	
	@Transactional 
	public void promotesEvent (Promoter promoter, Event event) {
//		String[] errorMessages = { "Promoter needs to be selected for promotes!", "Promoter does not exist!",
//				"Event needs to be selected for registration.", "Event does not exist!", "Promoter has already registered in the event!"};
		String error = "";
		// no promoter selected
		if (promoter == null) {
			// throw error
			error = error + "Promoter needs to be selected for promotes!";
		} else if (promoter.getPromotes() == null) {
			Set<Event> promotedEvents = new HashSet<Event>();
			promotedEvents.add(event);
			promoter.setPromotes(promotedEvents);
		}
		if(event == null) {
			 error = error + "Event needs to be selected!";
		 }
//			// promoter that does not exist
//		} else if (!promoterRepository.existsById(promoter.getName())) {
//			error = error + "Person does not exist!";
//		}
		// check if event has been selected and then check if it exists
//		if (event == null) {
//			error = error + "Event needs to be selected for registration!";
		 if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		 
//		// check if promoter has registered or have promoted the event
//		if (promoterRepository.existsByPromoterAndEvent(promoter, event)) {
//			error = error + "Promoter needs to be selected for promotes!";
//		}
		
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		promoterRepository.save(promoter);

	}
	
	@Transactional
	public List<Event> getEventsAttendedByPromoter(Promoter promoter) {
		if (promoter == null) {
			throw new IllegalArgumentException("Promoter cannot be null!");
		}
		List<Event> eventsAttendedByPromoter = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(promoter)) {
			eventsAttendedByPromoter.add(r.getEvent());
		}
		return eventsAttendedByPromoter;
	}
	
	@Transactional
	public List<Event> getEventsPromotedByPromoter(Promoter promoter) {
		if (promoter == null) {
			throw new IllegalArgumentException("Promoter cannot be null!");
		}
		List<Event> eventsPromotedByPromoter = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(promoter)) {
			eventsPromotedByPromoter.add(r.getEvent());
		}
		return eventsPromotedByPromoter;
	}
	
	@Transactional
	public Registration getRegistrationByPromoterAndEvent(Promoter promoter, Event event) {
		if (promoter == null || event == null) {
			throw new IllegalArgumentException("Promoter or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(promoter, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPromoter(Promoter promoter){
		if(promoter == null) {
			throw new IllegalArgumentException("Promoter cannot be null!");
		}
		return registrationRepository.findByPerson(promoter);
	}
	
	/*
	 * ============================
	 * END PROMOTER SERVICE METHODS
	 * ============================
	 */	
	
	/*
	 * ============================
	 *  CIRCUS SERVICE METHODS
	 * ============================
	 */	
	@Transactional
	public List<Circus> getAllCircuses() {
		return toList(circusRepository.findAll());
	}
	
	@Transactional
	public Circus createCircus(String name, Date circusDate, Time startTime, Time endTime, String company) {
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty!";
//			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		if (circusDate == null) {
			error = error + "Event date cannot be empty!";
//			throw new IllegalArgumentException("Event date cannot be empty!");
		} 
		if (startTime == null) {
			error = error + "Event start time cannot be empty!";
//			throw new IllegalArgumentException("Event start time cannot be empty!");
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty!";
//			throw new IllegalArgumentException("Event end time cannot be empty!");
		} else if ((endTime.before(startTime))) {
			error = error + "Event end time cannot be before event start time!";
//			if (endTime != null && startTime != null && endTime.before(startTime))
//			throw new IllegalArgumentException("Event end time cannot be before event start time!");
		}
		if (company == null || company.trim().length() == 0) {
			error = error + "Circus company cannot be empty!";
//			throw new IllegalArgumentException("Circus company cannot be empty!");
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		Circus event = new Circus();
		event.setName(name);
		event.setDate(circusDate);
		event.setStartTime(startTime); //convert Time to LocalTime
		event.setEndTime(endTime);
		event.setCompany(company);
		
		circusRepository.save(event);
		return event;
	}
	
	/*
	 * ============================
	 * END CIRCUS SERVICE METHODS
	 * ============================
	 */	
	
	/*
	 * ============================
	 * PAYMENT SERVICE METHODS
	 * ============================
	 */	
	@Transactional
	public ApplePay createApplePay(String id, int amount) {
		ApplePay applePay = new ApplePay();
		
		
		String[] parts = {};
		String letters = "";
		String numbers = "";
		
//		while (id!=null) {
		if (id != null) {
			if (id.contains("-")) {
				parts = id.split("-");
				numbers = parts[0];
				letters = parts[1];
			} else 	if (id.contains(" ") || id.contains("")) {
				String fullId = id.trim();
				int mid = fullId.length() / 2; 
				String firstHalf = fullId.substring(0, mid);
				String secondHalf = fullId.substring(mid);
				if (isInteger(firstHalf) ) {
					numbers = firstHalf;
				} 
				if (isAlpha(secondHalf)) {
					letters = secondHalf;
				}
		
			}
		}
		
		String error = "";
	
		if (id != null) {
			if (isInteger(numbers) == false || isAlpha(letters) == false || parts.length != 2) {
				error = error + "Device id is null or has wrong format!";
			} 
		} else if (id == null || id.trim().length() == 0) {
			error = error + "Device id is null or has wrong format!";
		}
		
		if (amount < 0) {
			error = error + "Payment amount cannot be negative!";
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		applePay.setDeviceID(id);
		applePay.setAmount(amount);
	
		applePayRepository.save(applePay);

		return applePay;
	}
	
	@Transactional 
	public void pay(Registration registration, ApplePay applePay) {
		String error = "";
		
		if (registration == null || applePay == null) {
			error = error + "Registration and payment cannot be null!";
		}
		
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		registration.setApplePay(applePay);
		registrationRepository.save(registration);
				
		
	}
	// helpers
	public boolean isAlpha (String str) {
	    char[] chars = str.toCharArray();
	    for (char c : chars) {
	        if (!Character.isLetter(c)) {
	            return false;
	        }
	    }
	    return true;
	}

	public boolean isInteger (String str) {
	   try
	   {
	      Integer.parseInt (str);
	      return true;
	   }
	   catch (Exception e)
	   {
	      return false;
	   }
	}
	/*
	 * ============================
	 * END PAYMENT SERVICE METHODS
	 * ============================
	 */	

	/*
	 * ============================
	 * EXTRA SERVICE METHODS
	 * ============================
	 */	
	
	@Transactional
	public Circus getCircus(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Circus name cannot be empty!");
		}
		Circus circus = circusRepository.findByName(name);
		return circus;
	}
	/*
	 * ============================
	 * END EXTRA SERVICE METHODS
	 * ============================
	 */	
	
	// End of implemented methods
	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

}
