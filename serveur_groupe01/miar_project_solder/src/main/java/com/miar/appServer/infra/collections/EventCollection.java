package com.miar.appServer.infra.collections;

import com.miar.appServer.entities.Event;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCollection extends MongoRepository<Event, String> {
    List<Event> findByName(String name);
    Event findByCreationDate(Date date);
    void delete(Event event);
}
