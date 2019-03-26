package com.miar.appServer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.miar.appServer.application.api.JsonConverter;
import com.miar.appServer.domain.EventService;
import com.miar.appServer.domain.SpentService;
import com.miar.appServer.domain.UserService;
import com.miar.appServer.entities.Event;
import com.miar.appServer.entities.Spent;
import com.miar.appServer.entities.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class EventController {
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    SpentService spentService;
    
    @Autowired
    private ObjectMapper mapper;
    
    @GetMapping("/evenements")
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAll(), HttpStatus.OK);
    }
    
    @PostMapping("/evenement")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/evenement")
    public ResponseEntity<?> modifyEvent(@RequestBody Event event) {
        boolean result = eventService.modifyEvent(event);
        return result ? new ResponseEntity<>(HttpStatus.OK) :  ResponseEntity
        		.status(HttpStatus.CONFLICT)
        		.body("date added before today's date");
    }
    
    @GetMapping("/evenement/search/{name}")
    public ResponseEntity<List<Event>> getEvent(@PathVariable String name) {
        List<Event> events = eventService.getEvent(name);
        return (events != null) ? new ResponseEntity<>(events, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/evenement/{idEvent}")
    public ResponseEntity<ObjectNode> getEventById(@PathVariable String idEvent) throws JSONException {
        Event event = eventService.getEventByid(idEvent);
        
        ObjectNode res = mapper.createObjectNode();
        
        ArrayNode user = mapper.createArrayNode();
        if(spentService.getBalances(idEvent) != null) {
        	 for (User u: spentService.getBalances(idEvent)) {
             	user.add(JsonConverter.convertUserForEvent(idEvent, eventService, spentService, mapper, u));
             }
        }
       
        
        ArrayNode spents = mapper.createArrayNode();
        for (Spent s : spentService.getSpents(idEvent)) {
        	spents.add(JsonConverter.convertSpent(mapper , s));
        }
        res.put("id", event.id);
        res.put("name", event.name);
        res.put("description", event.description);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
        res.put("supposedDate", dt.format(event.supposedDate));
        res.put("place", event.place);
        res.put("contributors", user);
        res.put("spents", spents);
        res.put("creator", JsonConverter.convertUserForEvent(idEvent, eventService, spentService, mapper, userService.getUser(event.creatorPseudo)));

        return (res != null) ? new ResponseEntity<>(res, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping("/evenement/{id}")
    public ResponseEntity<?> removeEvent(@PathVariable String id ) {
        boolean result = eventService.removeEvent(id);
        return result ? new ResponseEntity<>(HttpStatus.OK) : ResponseEntity.status(HttpStatus.CONFLICT)
        		.body("the event isn't database");
    }

    @PostMapping("/evenement/membre/{id}/{pseudoContributor}")
    public ResponseEntity<?> addContributor(@PathVariable("id") String eventId, @PathVariable("pseudoContributor") String pseudo) {
        boolean result = eventService.addContributor(eventId, pseudo);
        return result ? new ResponseEntity<>(HttpStatus.OK) : ResponseEntity.status(HttpStatus.CONFLICT)
        		.body("the event isn't database");
    }

    @GetMapping("/evenements/{pseudoCreateur}")
    public ResponseEntity<List<Event>> getCreatedEvents(@PathVariable("pseudoCreateur") String pseudo) {
        List<Event> createdEvents = userService.getCreatedEvents(pseudo);
        return (createdEvents != null) ? new ResponseEntity<>(createdEvents, HttpStatus.OK) 
        		: new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // list of all membres
    @GetMapping("/evenement/membres/{id}")
    public ResponseEntity<String> getContributors(@PathVariable("id") String eventId) throws JSONException {
        List<User> contributors = eventService.getContributors(eventId);
        JSONArray array = new JSONArray();
        User contributor;
        for (User c: contributors) {
            contributor = userService.getUser(c.pseudo);
            array.put(JsonConverter.convertUser(contributor));
        }
        return (array != null) ? new ResponseEntity<>(array.toString(), HttpStatus.OK) 
        		: new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/evenement/membre/{id}/{pseudoParticipant}")
    public ResponseEntity<Boolean> removeContributor(@PathVariable("id") String eventId, @PathVariable("pseudoParticipant") String pseudo) {
        boolean result = eventService.removeContributor(eventId, pseudo);
        return result ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

}
