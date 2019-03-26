package com.miar.appServer.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.miar.appServer.domain.EventService;
import com.miar.appServer.domain.SpentService;
import com.miar.appServer.entities.Event;
import com.miar.appServer.entities.Spent;
import com.miar.appServer.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonConverter {
    public static JSONObject convertUser(User user) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("pseudo", user.pseudo);
        json.put("balance", user.balance);
        json.put("created", user.created);
        json.put("invited", user.invited);
        return json;
    }
    public static ObjectNode convertUserForEvent(String idEvenement, EventService eventService, SpentService spentService, ObjectMapper mapper, User user) throws JSONException {
    	ObjectNode json = mapper.createObjectNode();
        json.put("pseudo", user.pseudo);
        json.put("balance", user.balance);
        
        Event e = eventService.getEventByid(idEvenement);
        double res = 0.00;
        int i = 0;
        for (String idSpent : e.spents) {
			Spent actually = spentService.getSpent(idSpent);
			if (actually.creatorPseudo.equals(user.pseudo)) {
				res += actually.amount;
				i++;
			}
		}        
        json.put("total", res);
        
        json.put("average", res / (i == 0 ? 1 : i));
        return json;
    }
    
    public static ObjectNode convertUser(ObjectMapper mapper, User user) throws JSONException {
    	ObjectNode json = mapper.createObjectNode();
        json.put("pseudo", user.pseudo);
        json.put("balance", user.balance);
        return json;
    }
    
    public static ObjectNode convertSpent(ObjectMapper mapper, Spent s) throws JSONException {
    	ObjectNode json = mapper.createObjectNode();
        json.put("label", s.spentId);
        json.put("label", s.label);
        json.put("amount", s.amount);
        return json;
    }
}
