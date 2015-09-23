package demo.api.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.JerseyDemoApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JerseyDemoApplication.class)
public class JerseyDemoApplicationIT {

	@Test
	public void contextLoads() {
	}

}
