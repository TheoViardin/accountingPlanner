package com.miar.appServer.application.controllers;

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

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;    
    @Autowired
    SpentService spentService;
    @GetMapping("/membres")
    public ResponseEntity<String> getAllUsers() throws JSONException {
        JSONArray array = new JSONArray();
        JSONObject json;
        List<User> users = userService.getAll();
        User user;
        for (User u: users) {
            user = userService.getUser(u.pseudo);
            array.put(JsonConverter.convertUser(user));
        }
        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }

    @GetMapping("/membre/{pseudo}")
    public ResponseEntity<String> getUser(@PathVariable ("pseudo") String pseudo) throws JSONException {
        User user = userService.getUser(pseudo);
        return (user != null) ? new ResponseEntity<>(JsonConverter.convertUser(user).toString(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/membre/average/{idEvenement}/{pseudo}")
    public ResponseEntity<Double> getAvg(@PathVariable ("pseudo") String pseudo, @PathVariable ("idEvenement") String idEvenement) throws JSONException {
        Event e = eventService.getEventByid(idEvenement);
        double res = 0.00;
        int i = 0;
        for (String idSpent : e.spents) {
			Spent actually = spentService.getSpent(idSpent);
			if (actually.creatorPseudo.equals(pseudo)) {
				res += actually.amount;
				i++;
			}
		}
        res = res / (i == 0 ? 1 : i);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/membre/total/{idEvenement}/{pseudo}")
    public ResponseEntity<Double> getTotal(@PathVariable ("pseudo") String pseudo, @PathVariable ("idEvenement") String idEvenement) throws JSONException {
        Event e = eventService.getEventByid(idEvenement);
        
        double res = 0.00;
        
        for (String idSpent : e.spents) {
			Spent actually = spentService.getSpent(idSpent);
			if (actually.creatorPseudo.equals(pseudo)) {
				res += actually.amount;
			}
		}
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/membre/{pseudo}/{oldpass}/{newpass}/{oldmail}/{newmail}")
    public ResponseEntity<HttpStatus> modifyUser(@PathVariable String pseudo, @PathVariable String oldpass, @PathVariable String newpass,  @PathVariable String oldmail,  @PathVariable String newmail ) {
        return userService.modifyUser(pseudo, oldpass, newpass, oldmail, newmail);
    }
    

    // --- Authentification: login and register

    @PostMapping("/membre/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody User user) {
        boolean result = userService.loginUser(user);
        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/membre")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/membre/{pseudo}")
    public ResponseEntity<Boolean> removeUser(@PathVariable String pseudo ) {
        boolean result = userService.removeUser(pseudo);
        return result ? new ResponseEntity<>(true, HttpStatus.OK) : new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
    }
}
