package demo.api.rest;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.JerseyDemoApplication;
import demo.model.Artist;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JerseyDemoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ArtistEndpointIT {

	private RestTemplate restTemplate = new TestRestTemplate();

	@Value("${local.server.port}")
	private int port;

	private String baseURL;

	@Before
	public void init() {
		baseURL = "http://localhost:" + port + "/api/artists";
	}
	
	@Test
	public void testEndpoint_WhenFindArtists_ThenArtistsAreReturned() {		
		ParameterizedTypeReference<List<Artist>> artistListType = new ParameterizedTypeReference<List<Artist>>() {};		
		ResponseEntity<List<Artist>> entity = restTemplate.exchange(baseURL, HttpMethod.GET, null, artistListType);
		
		assertThat(entity.getStatusCode().is2xxSuccessful(), is(true));		
		List<Artist> artists = entity.getBody(); 
		assertThat(artists, hasSize(greaterThan(0)));
	}

	@Test
	public void testEndpoint_WhenFindArtistById_ThenArtistIsReturned() {
		String resourceUrl = baseURL + "/1";	
		ResponseEntity<Artist> entity = restTemplate.getForEntity(resourceUrl, Artist.class);

		assertThat(entity.getStatusCode().is2xxSuccessful(), is(true));
		assertThat(entity.getBody().getId(), is("1"));
	}

	@Test
	public void testEndpoint_WhenRemoveArtistById_ThenArtistIsRemoved() {
		String resourceUrl = baseURL + "/1";		
		restTemplate.delete(resourceUrl);

		assertThatResourceHasBeenRemoved(resourceUrl);		
	}
	
	
	@Test
	public void testEndpoint_WhenSaveArtist_ThenStatusIsCreated() throws JsonProcessingException {
		Artist artist = new Artist();
		artist.setName("BrandNewArtist");
		
		ResponseEntity<Artist> entity = restTemplate.exchange(baseURL, HttpMethod.POST, prepareJsonRequest(artist), Artist.class);
		
		assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
	}
	
	@Test
	public void testEndpoint_WhenSaveArtist_ArtistIsSaved() throws JsonProcessingException {
		Artist artist = new Artist();
		artist.setName("BrandNewArtist");
		
		ResponseEntity<Artist> entity = restTemplate.exchange(baseURL, HttpMethod.POST, prepareJsonRequest(artist), Artist.class);
		
		String resourceUrl = entity.getHeaders().getLocation().toString();		
		Artist dbArtist = readResourceFromDatabase(resourceUrl);
		assertThat(dbArtist.getId(), is(notNullValue()));
	}
	
	@Test
	public void testEndpoint_WhenUpdateExistingArtist_ArtistIsUpdated() throws JsonProcessingException {
		Artist artist = new Artist();
		artist.setName("NewName");

		String resourceUrl = baseURL + "/1";
		restTemplate.exchange(resourceUrl, HttpMethod.PUT, prepareJsonRequest(artist), Artist.class);
				
		Artist dbArtist = readResourceFromDatabase(resourceUrl);		
		assertThat(dbArtist.getName(), is("NewName"));
	}	
	
	private Artist readResourceFromDatabase(String resourceUrl){
		ResponseEntity<Artist> entity = restTemplate.getForEntity(resourceUrl, Artist.class);		
		assertThat(entity.getStatusCode().is2xxSuccessful(), is(true));
		return entity.getBody();
	}
	
	private void assertThatResourceHasBeenRemoved(String resourceUrl){
		ResponseEntity<Artist> entity = restTemplate.getForEntity(resourceUrl, Artist.class);		
		assertThat(entity.getStatusCode(), is(HttpStatus.NO_CONTENT));		
	}
	
	private HttpEntity<String> prepareJsonRequest(Object object) throws JsonProcessingException{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<String>(toJson(object), headers);
	}
	
	private String toJson(Object object) throws JsonProcessingException{
		return new ObjectMapper().writeValueAsString(object);
	}
}
