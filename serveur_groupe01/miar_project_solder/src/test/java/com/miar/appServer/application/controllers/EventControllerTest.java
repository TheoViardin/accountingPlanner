//package com.miar.appServer.application.controllers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.annotation.Transient;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.miar.appServer.domain.EventService;
//import com.miar.appServer.domain.UserService;
//import com.miar.appServer.entities.Event;
//import com.miar.appServer.entities.User;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class EventControllerTest {
//    @Autowired
//    private EventService eventService;
//
//    @Autowired
//    private UserService userService;
//
//	@Test
//    public void contexLoads() throws Exception {
//        assertThat(eventService).isNotNull();
//    }
//	@Test
//	public void testGetAllEvents() {
//		eventService.clear();
//        assertThat(eventService.getAll()).isNotNull().isEmpty();
//	}
//	@Test
//	public void testCreateEvent() {
//	    eventService.clear();
//		userService.clear();
//
//		User alex = new User();
// 	    alex.pseudo = "alexdu44";
// 	    alex.email = "alexdu44";
// 	    alex.mdp = "alexdu44";
//
//		 // given
// 	   userService.createUser(alex);
//
//	    // when
//	    User user = userService.getUser(alex.pseudo);
//
//		Event e = new Event();
//		e.name = "test";
//		e.place = "test";
//		e.creatorId = user.id;
//
//		eventService.createEvent(e);
//
//	    // when
//	    List<Event> found = eventService.getEvent(e.name);
//
//	    // then
//	    assertThat(found)
//	      .isNotEqualTo(null);
//
//
//	}
//
//
//	@Test
//	public void testRemoveEvent() {
//	    eventService.clear();
//		userService.clear();
//
//		User alex = new User();
// 	    alex.pseudo = "alexdu44";
// 	    alex.email = "alexdu44";
// 	    alex.mdp = "alexdu44";
//
//		 // given
// 	   userService.createUser(alex);
//
//	    // when
//	    User user = userService.getUser(alex.pseudo);
//
//		Event e = new Event();
//		e.id="1";
//		e.name = "test";
//		e.place = "test";
//		e.creatorId = user.id;
//
//		eventService.createEvent(e);
//		eventService.removeEvent(e.id);
//		 // when
//		List<Event> found = eventService.getEvent(e.name);
//	    User foundUser = userService.getUser(alex.pseudo);
//
//	    // then
//	    assertThat(found)
//	      .isEqualTo(null);
//
//	    eventService.clear();
//		userService.clear();
//	}
//
//	@Test
//	public void testAddContributor() {
//	    eventService.clear();
//		userService.clear();
//
//		User alex = new User();
// 	    alex.pseudo = "alexdu44";
// 	    alex.email = "alexdu44";
// 	    alex.mdp = "alexdu44";
//
//		 // given
// 	   userService.createUser(alex);
//
//	    // when
//	    User user = userService.getUser(alex.pseudo);
//
//		Event e = new Event();
//		e.id = "1";
//		e.name = "test";
//		e.place = "test";
//		e.creatorId = user.id;
//
//		eventService.createEvent(e);
//
//		User thomas = new User();
//		thomas.pseudo = "thomas";
//		thomas.email = "thomas";
//		thomas.mdp = "thomas";
//	 	userService.createUser(thomas);
//
// 	    eventService.addContributor(e.id, thomas.pseudo);
//
// 	    Event test = eventService.getEventByid(e.id);
//	    User testUser = userService.getUser(thomas.pseudo);
//
// 	    assertThat(test.contributors).isNotEmpty();
// 	    assertThat(thomas.invited).isNotEmpty();
//	}
//	/*
//
//	@Test
//	public void testGetCreatedEvents() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetContributors() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveContributor() {
//		fail("Not yet implemented");
//	}
//*/
//}
