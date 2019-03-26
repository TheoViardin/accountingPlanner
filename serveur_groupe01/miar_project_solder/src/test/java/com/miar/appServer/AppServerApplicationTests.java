package com.miar.appServer;

import com.miar.appServer.application.AppServerApplication;
import com.miar.appServer.domain.EventService;
import com.miar.appServer.domain.SpentService;
import com.miar.appServer.domain.UserService;
import com.miar.appServer.infra.collections.UserCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= AppServerApplication.class)
@WebMvcTest
public class AppServerApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	EventService eventService;
	@MockBean
	SpentService spentService;
	@MockBean
	UserService userService;

	@Test
	public void contextLoads() throws Exception {

		Mockito.when(userService.getAll()).thenReturn(
				Collections.emptyList()
		);

		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/membres")
						.accept(MediaType.APPLICATION_JSON)
		).andReturn();

		Mockito.verify(userService).getAll();
	}


}

