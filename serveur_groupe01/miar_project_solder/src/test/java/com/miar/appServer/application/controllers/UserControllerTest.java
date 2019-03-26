package com.miar.appServer.application.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.miar.appServer.application.api.JsonConverter;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.miar.appServer.entities.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController controller;

	@Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
	@Test
	public void testGetUser() throws JSONException {
		User alex = new User();
 	    alex.pseudo = "alexdu44";
 	    alex.email = "alexdu44";
 	    alex.mdp = "alexdu44";

		 // given
	    controller.createUser(alex);
        String expectedString = JsonConverter.convertUser(alex).toString();
        HttpStatus expectedStatus = HttpStatus.OK;

	    // when
	    ResponseEntity<String> found = controller.getUser(alex.pseudo);

	    // then
	    assertThat(found.getBody())
	      .isEqualTo(expectedString);
	    assertThat(found.getStatusCode()).isEqualTo(expectedStatus);
	}

	@Test
	public void testModifyUser_OK() throws JSONException {
		User alex = new User();
 	    alex.pseudo = "alexdu44";
 	    alex.email = "alexdu44";
 	    alex.mdp = "alexdu44";
	    controller.createUser(alex);

		 // expected output
        ResponseEntity<HttpStatus> found = controller.modifyUser("alexdu44", "alexdu44", "alexdu44000", "alexdu44", "alexdu44@mail.com");
        HttpStatus expectedStatus = HttpStatus.OK;

		controller.removeUser(alex.pseudo);
	    // then
	    assertThat(found.getStatusCode())
	      .isEqualTo(expectedStatus);
	}

	@Test
    public void testModifyUser_Conflict() throws JSONException {
        User alex = new User();
        alex.pseudo = "alexdu44";
        alex.email = "alexdu44";
        alex.mdp = "alexdu44";
        controller.createUser(alex);

        // user input the same mail/pass
        ResponseEntity<HttpStatus> found = controller.modifyUser("alexdu44", "alexdu44", "alexdu44", "alexdu44", "alexdu44@mail.com");
        HttpStatus expectedStatus = HttpStatus.CONFLICT;

		controller.removeUser(alex.pseudo);
        // then
        assertThat(found.getStatusCode())
                .isEqualTo(expectedStatus);

    }


	@Test
	public void testLoginUser() {
		User alex = new User();
 	    alex.pseudo = "alexdu44";
 	    alex.email = "alexdu44";
 	    alex.mdp = "alexdu44000";
	    controller.createUser(alex);

	    ResponseEntity<HttpStatus> found = controller.loginUser(alex);
	    HttpStatus expectedStatus = HttpStatus.OK;

	    // then
	    assertThat(found.getStatusCode())
	      .isEqualTo(expectedStatus);

	    alex.mdp = "wrong_pass";
		ResponseEntity<HttpStatus> found2 = controller.loginUser(alex);
		HttpStatus expectedStatus2 = HttpStatus.NOT_ACCEPTABLE;
		controller.removeUser(alex.pseudo);
		assertThat(found2.getStatusCode())
				.isEqualTo(expectedStatus2);
	}

	@Test
	public void testRemoveUser() throws JSONException {
		User alex = new User();
 	    alex.pseudo = "alexdu44";
 	    alex.email = "alexdu44";
 	    alex.mdp = "alexdu44";
	    controller.createUser(alex);
	    // then
		controller.removeUser(alex.pseudo);
		ResponseEntity<String> found2 = controller.getUser(alex.pseudo);
	    assertThat(found2.getStatusCode())
	      .isEqualTo(HttpStatus.NO_CONTENT);
	}

}
