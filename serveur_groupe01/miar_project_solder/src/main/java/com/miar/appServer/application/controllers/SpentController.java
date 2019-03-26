package com.miar.appServer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.miar.appServer.application.api.JsonConverter;
import com.miar.appServer.domain.EventService;
import com.miar.appServer.domain.SpentService;
import com.miar.appServer.domain.UserService;
import com.miar.appServer.entities.Spent;
import com.miar.appServer.entities.User;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenement")
public class SpentController {
    @Autowired
    SpentService spentService;
    @Autowired
    private ObjectMapper mapper;
    @PostMapping("/depense/{id}/{pseudo}")
    public ResponseEntity<HttpStatus> addSpentToEvent(@RequestBody Spent spent, @PathVariable("id") String eventId, @PathVariable String pseudo) {
        boolean result = spentService.addSpentToEvent(eventId, pseudo, spent);
        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @GetMapping("/depenses/{id}")
    public ResponseEntity<List<Spent>> getSpents(@PathVariable("id") String eventId) {
        List<Spent> spents = spentService.getSpents(eventId);
        return (spents != null) ? new ResponseEntity<>(spents, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/depenses/{id}/{pseudo}")
    public ResponseEntity<List<Spent>> getSpentsOfUser(@PathVariable("id") String eventId, @PathVariable("pseudo") String pseudo) {
        List<Spent> spents = spentService.getSpentsOfUser(eventId, pseudo);
        return (spents != null) ? new ResponseEntity<>(spents, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/depenses/total/{id}")
    public ResponseEntity<Double> getTotal(@PathVariable("id") String eventId) {
        Double result = spentService.getTotal(eventId);
        return (result != null) ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }
    @GetMapping("/depenses/cout/{id}")
    public ResponseEntity<Double> getAvg(@PathVariable("id") String eventId) {
        Double result = spentService.getAvg(eventId);
        return (result != null) ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @GetMapping("/depenses/solde/{id}")
    public ResponseEntity<ArrayNode> contributors(@PathVariable("id") String eventId) throws JSONException {
        List<User> users = spentService.getBalances(eventId);
        ArrayNode res = mapper.createArrayNode();
        for (User u: users) {
        	res.add(JsonConverter.convertUser(mapper, u));
        }
        return (res != null) ? new ResponseEntity<>(res, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @GetMapping("/depense/solde/{idEvent}/{pseudo}")
    public ResponseEntity<User> contributors(@PathVariable("idEvent") String eventId, @PathVariable("pseudo") String pseudo) {
        User user = spentService.getBalance(eventId, pseudo);
        return (user != null) ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @DeleteMapping("/depense/{idEvent}")
    public ResponseEntity<Boolean> removeSpent(@PathVariable("idEvent") String idEvent, @RequestBody List<String> idSpents) {
        boolean result = spentService.removeSpent(idEvent, idSpents);
        return result ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
}
