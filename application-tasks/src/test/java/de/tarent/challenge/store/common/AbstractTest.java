package de.tarent.challenge.store.common;

import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest  {
	
	@LocalServerPort
	private int serverPort;
  
   protected void setUp() {
	   RestAssured.port = serverPort;
   }

   
   
}
