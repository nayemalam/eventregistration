package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Promoter{
private String name;

	private int id;
	
	public void setId(int value) {
		this.id = value;
	}

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
    }
    
	private Promoter promoter;
	
//	@ManyToOne(optional = false)
	public Promoter getPromoter () {
		return this.promoter;
	}
	
	public void setPromoter (Promoter promoter) {
		this.promoter = promoter;
	}
	
	private Event event;
	// 1 promoter can promote many events
//	@ManyToOne(optional = false)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	// gets promoted events
//	@ManyToOne(optional = false)
	public Event getPromotes () {
		return this.event;
	}
	
	public void setPromotes (Event event) {
		this.event = event;
	}

}
