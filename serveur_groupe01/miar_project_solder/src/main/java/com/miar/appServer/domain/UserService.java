package com.miar.appServer.domain;

import com.miar.appServer.entities.Event;
import com.miar.appServer.entities.User;
import com.miar.appServer.infra.collections.UserCollection;

import org.apache.tomcat.util.http.parser.HttpParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    UserCollection userCollection;
    
    @Autowired
    EventService eventService;

    public List<User> getAll() { return userCollection.findAll(); }

    public ResponseEntity<HttpStatus> createUser(User user) {
        user.balance = 0.0; //set balance to zero for a new user
        ResponseEntity<HttpStatus> status = isValidUser(user);
        if (!status.getStatusCode().isError()){
            userCollection.insert(user);
        }
        return status;
    }

    public boolean loginUser(User user) {
        for( User db_user : userCollection.findAll() )
        {
            if( db_user.pseudo.equals( user.pseudo ) )
            {
                if (db_user.mdp.equals(user.mdp)) {
                    return true;
                }
            }
        }
        return false;
    }

    public User getUser(String pseudo) {
        User user = userCollection.findByPseudo(pseudo);
        if (user == null) {
            return null;
        }
        return user;
    }

    public ResponseEntity<HttpStatus> modifyUser(String pseudo, String oldpass, String newpass, String oldmail, String newmail) {
        User user = userCollection.findByPseudo(pseudo);
        ResponseEntity<HttpStatus> status = changeCredentials(user, oldpass, newpass, oldmail, newmail);
        if (status.getStatusCode() == HttpStatus.OK) {
            user.mdp = newpass;
            user.email = newmail;
            userCollection.save(user);
        }
        return status;
    }

    // add security - only authorized user can delete account
    public boolean removeUser(String pseudo) {
        User user =  userCollection.findByPseudo(pseudo);
        if (user == null) {
            return false;
        }
        // delete events created by user
    	for (String eventId : user.created) {
    		eventService.removeEvent(eventId);
        }
    	
        userCollection.delete(user);
    	return true;
    }

    public List<Event> getCreatedEvents(String pseudo) {
        User user = userCollection.findByPseudo(pseudo);
        List<Event> createdEvents = new ArrayList<>();
        Event currentEvent;
        if (user == null){
            return null;
        }

        for (String eventId: user.created) {
            currentEvent = eventService.getEventByid(eventId);
            createdEvents.add(currentEvent);
        }
        return createdEvents;
    }
    
    public void clear() {
        for(User u : getAll()) {
        	removeUser(u.pseudo);;
        }
     }

    private ResponseEntity isValidUser(User user )
    {
        if (user.pseudo == null || user.email == null || user.mdp == null) {
            return ResponseEntity
            		.status(HttpStatus.EXPECTATION_FAILED)
            		.body(
            		user.pseudo == null ? "the username is not present\n" : "" +
            		user.email == null ? "the email is not present\n" : "" +
            		user.mdp == null  ? "the password is not present\n" : "");
        }
        if (user.pseudo.equals("") || user.mdp.length() < 3) {
        	boolean test = user.mdp.length() < 3;
            return  ResponseEntity
            		.status(HttpStatus.LENGTH_REQUIRED)
            		.body(
            				(user.pseudo.equals("") ? "the username is empty\n" : "" )+
            				(user.mdp.length() <= 3 ? "the password is less than 3 characters\n" : ""));
        }
        for( User db_user : userCollection.findAll() )
        {
            if( db_user.pseudo.equals( user.pseudo ) || db_user.email.equals(user.email))  // check if choosed pseudo exists already in db
            {
                return ResponseEntity
                		.status(HttpStatus.CONFLICT)
                		.body((db_user.pseudo.equals( user.pseudo ) ? "the pseudo already exists" : "")+
                        	(db_user.email.equals( user.email ) ? "the email already exists" : ""));
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity changeCredentials(User user, String oldpass, String newpass, String oldmail, String newmail) {
        if (user == null) {
            return ResponseEntity
            		.status(HttpStatus.BAD_REQUEST)
            		.body("there is no user in the request");
        }
        if (!user.mdp.equals(oldpass) || !user.email.equals(oldmail)) {
            return ResponseEntity
            		.status(HttpStatus.EXPECTATION_FAILED)
            		.body((!user.mdp.equals(oldpass) ? "the password is not good" : "") + 
            			(!user.email.equals(oldmail) ? "the email is not good" : ""));
        }
        if (newpass.equals(oldpass) || newmail.equals(oldmail)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getUser(username);
	}

}
