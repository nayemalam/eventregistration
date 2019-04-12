package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class PromoterDto {

	private String name;
	private List<EventDto> eventsAttended;
	private List<EventDto> eventsPromoted;

	public PromoterDto() {
	}

	@SuppressWarnings("unchecked")
	public PromoterDto(String name) {
		this(name, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	public PromoterDto(String name, List<EventDto> events, List<EventDto> promotedEvents) {
		this.name = name;
		this.eventsAttended = events;
		this.eventsPromoted = promotedEvents;
	}

	public String getName() {
		return name;
	}

	public List<EventDto> getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(List<EventDto> events) {
		this.eventsAttended = events;
	}
	
	public List<EventDto> getEventsPromoted() {
		return eventsPromoted;
	}

	public void setEventsPromoted(List<EventDto> promotedEvents) {
		this.eventsPromoted = promotedEvents;
	}
}
