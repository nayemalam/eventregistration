package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.eventregistration.dto.ApplePayDto;
import ca.mcgill.ecse321.eventregistration.dto.CircusDto;
import ca.mcgill.ecse321.eventregistration.dto.EventDto;
import ca.mcgill.ecse321.eventregistration.dto.PersonDto;
import ca.mcgill.ecse321.eventregistration.dto.PromoterDto;
import ca.mcgill.ecse321.eventregistration.dto.RegistrationDto;
import ca.mcgill.ecse321.eventregistration.model.ApplePay;
import ca.mcgill.ecse321.eventregistration.model.Circus;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Promoter;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/EventRegistrationRestController")
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}
	
	@PostMapping(value = { "/promoters/{name}", "/promoters/{name}/" })
	public PromoterDto createPromoter(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Promoter promoter = service.createPromoter(name);
		return convertToDto(promoter);
	}
	
	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}
	
	@PostMapping(value = { "/events/circus/{name}", "/events/circus/{name}/" })
	public CircusDto createCircus(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, @RequestParam String company)
			throws IllegalArgumentException {
		// @formatter:on
		Circus circus = service.createCircus(name, date, Time.valueOf(startTime), Time.valueOf(endTime), company);
		return convertToDto(circus);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	@PostMapping(value = { "/assign", "/assign/" })
	public RegistrationDto registerPromoterForEvent(@RequestParam(name = "promoter") PromoterDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the promoter and the event are identified by their names
		Promoter p = service.getPromoter(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	
	@PostMapping(value = { "/applepay/{deviceId}", "/applepay/{deviceId}/" })
	public ApplePayDto createApplePay(@PathVariable("deviceId") String deviceId, @RequestParam int amount) throws IllegalArgumentException {
		ApplePay applePay = service.createApplePay(deviceId, amount);
		return convertToDto(applePay);
	}	

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	} 
	
	@GetMapping(value = { "/events/circus", "/events/circus/" })
	public List<CircusDto> getAllCircuses() {
		List<CircusDto> circusDtos = new ArrayList<>();
		for (Circus circus : service.getAllCircuses()) {
			circusDtos.add(convertToDto(circus));
		}
		return circusDtos;
	}
	
	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}
	
	@GetMapping(value = { "/promoters/{name}", "/promoters/{name}/" })
	public PromoterDto getPromoterByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPromoter(name));
	}
	
	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}
	
	@GetMapping(value = { "/assignations", "/assignations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "promoter") PromoterDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Promoter p = service.getPromoter(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPromoterAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}
	
	@GetMapping(value = { "/assignations/promoter/{name}", "/assignations/promoter/{name}/" })
	public List<RegistrationDto> getRegistrationsForPromoter(@PathVariable("name") PromoterDto pDto)
			throws IllegalArgumentException {
		// Both the promoter and the event are identified by their names
		Promoter p = service.getPromoter(pDto.getName());
		return createRegistrationDtosForPromoter(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}
	
	@GetMapping(value = { "/promoters", "/promoters/" })
	public List<PromoterDto> getAllPromoters() {
		List<PromoterDto> promoters = new ArrayList<>();
		for (Promoter promoter : service.getAllPromoters()) {
			promoters.add(convertToDto(promoter));
		}
		return promoters;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	
	@GetMapping(value = { "/applepay/{deviceId}", "/applepay/{deviceId}/" })
	public ApplePayDto getApplePayById(@PathVariable("deviceId") String deviceId) throws IllegalArgumentException {
		return convertToDto(service.getApplePay(deviceId));
	}
	
	@GetMapping(value = { "/applepay", "/applepay/" })
	public List<ApplePayDto> getAllPayments() {
		List<ApplePayDto> applePays = new ArrayList<>();
		for (ApplePay applePay : service.getAllPayments()) {
			applePays.add(convertToDto(applePay));
		}
		return applePays;
	}

	// Model - DTO conversion methods (not part of the API)
	
	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}
	
	private CircusDto convertToDto(Circus e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Circus!");
		}
		CircusDto eventDto = new CircusDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getCompany());
		return eventDto;
	}
	
	private PromoterDto convertToDto(Promoter p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Promoter!");
		}
		PromoterDto promoterDto = new PromoterDto(p.getName());
		promoterDto.setEventsAttended(createAttendedEventDtosForPromoter(p));
		promoterDto.setEventsPromoted(createPromotedEventDtosForPromoter(p));
		return promoterDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		
		return personDto;
	}
	
	private ApplePayDto convertToDto(ApplePay a) {
		if (a == null) {
			throw new IllegalArgumentException("There is no such Payment!");
		}
		ApplePayDto applePayDto = new ApplePayDto(a.getDeviceID(), a.getAmount());
		return applePayDto;
	}
	

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	// return registration dto without person object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}

	
	

	// Other extracted methods (not part of the API)
	
	private List<EventDto> createAttendedEventDtosForPromoter(Promoter p) {
		List<Event> eventsForPromoter = service.getEventsAttendedByPromoter(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPromoter) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
	private List<EventDto> createPromotedEventDtosForPromoter(Promoter p) {
		List<Event> eventsForPromoter = service.getEventsPromotedByPromoter(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPromoter) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
	private List<ApplePayDto> createApplePaidDtosForPerson(Person p) {
		List<ApplePay> paidForPerson = service.getPaidByPerson(p);
		List<ApplePayDto> payments = new ArrayList<>();
		for (ApplePay payment : paidForPerson) {
			payments.add(convertToDto(payment));
		}
		return payments;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
	
	private List<RegistrationDto> createRegistrationDtosForPromoter(Promoter p) {
		List<Registration> registrationsForPromoter = service.getRegistrationsForPromoter(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPromoter) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
}
