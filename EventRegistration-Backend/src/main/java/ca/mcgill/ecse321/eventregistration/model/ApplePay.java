package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApplePay {
	
	private String deviceId;
	
	public void setDeviceID(String deviceId) {
		this.deviceId = deviceId;
	}
	@Id
	public String getDeviceID() {
		return this.deviceId;
	}
	
	private int amount;
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}


	
//	private int id;
//
//	public void setId(int value) {
//		this.id = value;
//	}
//
//	@Id
//	public int getId() {
//		return this.id;
//	}
//
//	private Person person;
//
//	@ManyToOne(optional = false)
//	public Person getPerson() {
//		return this.person;
//	}
//
//	public void setPerson(Person person) {
//		this.person = person;
//	}
//
//	private Event event;
//
//	@ManyToOne(optional = false)
//	public Event getEvent() {
//		return this.event;
//	}
//
//	public void setEvent(Event event) {
//		this.event = event;
//	}
	
}
