package com.miar.appServer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.Date;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Spent {
    @Id
    public String spentId;
    public Date creationDate;
    public double amount;
    public String label;
    public String creatorPseudo;
    public String eventId;
	public String getEventId() {
		return eventId;
	}
    
    
}
