package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Promoter extends Person{
		
//
//   
//	private Promoter promoter;
//	
//	@ManyToOne(optional = true)
//	public Promoter getPromoter () {
//		return this.promoter;
//	}
//	
//	public void setPromoter (Promoter promoter) {
//		this.promoter = promoter;
//	}
//	
//	private Event event;
//	// 1 promoter can promote many events
//	@ElementCollection // (https://stackoverflow.com/questions/6164123/org-hibernate-mappingexception-could-not-determine-type-for-java-util-set)
//	@ManyToOne(optional = true)
//	public Event getEvent() {
//		return this.event;
//	}
//
//	public void setEvent(Event event) {
//		this.event = event;
//	}
//	
//	 gets promoted events (should be a list)
//	@ManyToOne(optional = true)
//	public Event getPromotes () {
//		// return a list of events
//		return this.event;
//	}
//	
//	public void setPromotes (Event event) {
//		this.event = event;
//	}
	
	private Set<Event> promotedEvent;
	
	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Event> getPromotes() {
	   return this.promotedEvent;
	}

	public void setPromotes(Set<Event> promotedEvent) {
	   this.promotedEvent = promotedEvent;
	}


}
