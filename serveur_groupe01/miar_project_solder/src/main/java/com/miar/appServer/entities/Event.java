package com.miar.appServer.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Event {
    @Id
    public String id;
    public String name;
    public String description;
    public Date creationDate;
    public Date supposedDate;
    public String place;
    public String creatorPseudo;
    public List<String> contributors  = new ArrayList();
    public List<String> spents = new ArrayList();
}
