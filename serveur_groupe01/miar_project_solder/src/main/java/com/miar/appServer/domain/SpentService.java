package com.miar.appServer.domain;

import com.miar.appServer.entities.Event;
import com.miar.appServer.entities.Spent;
import com.miar.appServer.entities.User;
import com.miar.appServer.infra.collections.EventCollection;
import com.miar.appServer.infra.collections.SpentCollection;
import com.miar.appServer.infra.collections.UserCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpentService {
	@Autowired
	UserCollection userCollection;
	@Autowired
	EventCollection eventCollection;
	@Autowired
	SpentCollection spentCollection;
	@Autowired
	EventService eventService;

	public boolean addSpentToEvent(String id, String pseudo, Spent spent) {
		// check the spent body
		if (spent == null || spent.amount == 0 || spent.label == null) return false;
		User contributor = userCollection.findByPseudo(pseudo);
		Event event = eventService.getEventByid(id);
		if (contributor == null || event == null) return false;
		// check if ths user who add spent is present in a list of contributors
		boolean flag = false;
		for (String c : event.contributors) {
			if (c.equals(pseudo)){
				flag = true;
				break;
			}
		}
		if (flag == false) return false;
		// following line is probably redundant if we establish creator of spent on a client side
		spent.creatorPseudo = contributor.pseudo;
		spent.eventId = id;
		//----------
		spent.creationDate = new Date();
		spentCollection.insert(spent);
		Spent addedSpent = spentCollection.findByCreationDate(spent.creationDate);
		event.spents.add(addedSpent.spentId);

		eventCollection.save(event); // save changes
		return true;
	}

	public List<Spent> getSpents(String id) {
		Event event = eventService.getEventByid(id);
		if (event == null) return null;
		List<Spent> spents = new ArrayList<>();
		Spent currentSpent;
		if (event.spents != null) {
			for (String spentId: event.spents) {
				currentSpent = spentCollection.findBySpentId(spentId);
				spents.add(currentSpent);
			}
		}
		return spents;
	}
	
	public Spent getSpent(String id) {
		if(!spentCollection.findById(id).isPresent()) {
			return null;
		}
		return spentCollection.findById(id).get();
	}

	public Double getTotal(String id) {
		Event event = eventService.getEventByid(id);
		if (event == null) return null;
		Spent spent;
		double total = 0;
		for (String spentId: event.spents) {
			spent = spentCollection.findBySpentId(spentId);
			total += spent.amount;
		}
		return total;
	}

	// the price par contributor (average)
	public Double getAvg(String id) {
		double total = getTotal(id);
		Event event = eventService.getEventByid(id);
		if (event == null) return null;
		if(total == 0) return 0.00;
		double avg = 0;
		if (total != 0) {
			avg = total/event.contributors.size();
		}
		return avg;
	}

	public List<User> getBalances(String id) {
		List<String> presented = new ArrayList<>();      // check if user participated in spents more than once
		Event event = eventService.getEventByid(id);
		if (event == null) return null;
		User user;
		Spent currentSpent;
		double average = getAvg(id);
		boolean isPresented;
		if (event.contributors.size() == 1){
			user = userCollection.findByPseudo(event.creatorPseudo);
			user.balance = average;
			userCollection.save(user);
		}
		else if (getTotal(id) != 0) {
			for (String spentId: event.spents) {
				currentSpent = spentCollection.findBySpentId(spentId);
				user = userCollection.findByPseudo(currentSpent.creatorPseudo);  // creator of spent
				isPresented = false;
				for (String p : presented) {
					if (user.pseudo.equals(p)) {    // user is already participated
						user.balance += currentSpent.amount;
						isPresented = true;
						break;
					}
				}
				if (!isPresented) {
					user.balance = currentSpent.amount - average;
					presented.add(user.pseudo); // add user in array of already participated
				}
				userCollection.save(user);
			}
			for (String contributorPseudo : event.contributors) {
				User contributor = userCollection.findByPseudo(contributorPseudo);
				if (contributor == null) return null;
				boolean flag = false;
				for (String p : presented) {
					if (p.equals(contributorPseudo)){
						flag = true;
						break;
					}
				}
				if (flag == false) {
					contributor.balance = -average;
					userCollection.save(contributor);
				}
			}
		}
		return eventService.getContributors(id);
	}


	public List<Spent> getSpentsOfUser(String eventId, String creatorPseudo) {
		return spentCollection.findByCreatorPseudoAndEventId(creatorPseudo, eventId);
	}

	public boolean removeSpent(String idEvent, List<String> idSpents) {
		Event e = eventCollection.findById(idEvent).get();
		for (Iterator<String> it = e.spents.iterator(); it.hasNext(); )
		{
			String spentId = it.next();
			for (Iterator<String> it2 = idSpents.iterator(); it2.hasNext(); )
			{
				String idSpent = it2.next();
				if( spentId.equals( idSpent ) )
				{
					it.remove();
					Spent s = getSpent(idSpent);
					spentCollection.delete(s);
				}
			}
		}
		eventCollection.save(e);
		return true;
	}

	public User getBalance(String eventId, String pseudo) {
		return null;
	}
}
