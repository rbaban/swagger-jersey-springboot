package demo.api.rest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import demo.api.rest.ArtistRestEndpoint;
import demo.model.Artist;
import demo.service.ArtistService;

@RunWith(MockitoJUnitRunner.class)
public class ArtistEndpointTest {

	@InjectMocks
	private ArtistRestEndpoint endpoint;
	
	@Mock
	private ArtistService service;
	
	@Mock
	private UriInfo uriInfo;
	
	@Test
	public void testFindAll_WhenCalled_ThenArtistsAreReturned() {
		Iterable<Artist> artists = new ArrayList<>();
		Mockito.when(service.findAll()).thenReturn(artists);
		
		Response response = endpoint.findAll();
		
		assertThat(response.getStatus(), is(200));		
		@SuppressWarnings("unchecked")
		Iterable<Artist> actualArtists = (Iterable<Artist>)response.getEntity();
		assertThat(actualArtists, is(artists));
	}
	
	@Test
	public void testFindOne_WhenArtistFound_ThenArtistIsReturned() {
		String id = "1";
		Artist artist = createDefaultArtist();
		Mockito.when(service.findOne(id)).thenReturn(artist);
		
		Response response = endpoint.findOne(id);
		
		Artist actualArtist = (Artist)response.getEntity();
		assertThat(response.getStatus(), is(200));				
		assertThat(actualArtist, is(artist));
	}
	
	@Test
	public void testFindOne_WhenArtistNotFound_ThenNullIsReturned() {
		String id = "1";
		Mockito.when(service.findOne(id)).thenReturn(null);
		
		Response response = endpoint.findOne(id);
		
		Artist actualArtist = (Artist)response.getEntity();		
		assertThat(response.getStatus(), is(204));				
		assertThat(actualArtist, is(nullValue()));
	}
	
	@Test
	public void testRemove_WhenSuccesfull_ThenServiceDeletesArtist() {
				
		String id = "1";
		Response response = endpoint.delete(id);
		
		assertThat(isSuccessfull(response), is(true));		
		Mockito.verify(service).delete(id);		
	}
	
	@Test
	public void testSave_WhenSuccesfull_ThenServiceCreatesArtist() throws Exception {
		Artist artist = createDefaultArtist();
		String artistUri = "http://localhost:8080/api/artists/newId";
		mockLocationHttpHeader(artistUri);
		
		endpoint.save(artist);
		
		Mockito.verify(service).save(artist);		
	}
	
	@Test
	public void testSave_WhenSuccesfull_ThenArtistUriIsSetOnLocationHeader() throws Exception {
		Artist artist = createDefaultArtist();
		artist.setId(null);
		
		String artistUri = "http://localhost:8080/api/artists/newId";
		mockLocationHttpHeader(artistUri);
		
		Response response = endpoint.save(artist);
		
		assertThat(isSuccessfull(response), is(true));
		assertThat(response.getLocation(), is(notNullValue()));
		assertThat(response.getLocation().toString(), is(artistUri));
	}
	
	@Test
	public void testUpdate_WhenSuccesfull_ThenServiceUpdatesArtist() throws Exception {
		Artist artist = createDefaultArtist();
		artist.setId("myId");
		
		Response response = endpoint.update(artist.getId(), artist);
		
		assertThat(isSuccessfull(response), is(true));
		Mockito.verify(service).save(artist);		
	}
	
	private void mockLocationHttpHeader(String resourceId) throws Exception {
		UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
		Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
		Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
		Mockito.when(mockUriBuilder.resolveTemplate(Mockito.anyString(), Mockito.anyObject())).thenReturn(mockUriBuilder);
		Mockito.when(mockUriBuilder.build()).thenReturn(new URI(resourceId));
	}
	
	private boolean isSuccessfull(Response response){
		return Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily());
	}

	private Artist createDefaultArtist() {
		Artist artist = new Artist();		
		return artist;
	}
	
	
}
