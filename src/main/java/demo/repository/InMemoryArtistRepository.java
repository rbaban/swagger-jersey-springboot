package demo.repository;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import demo.model.Artist;

@Component
public class InMemoryArtistRepository implements ArtistRepository{
	
	private Map<String, Artist> artists = new HashMap<String, Artist>();

	@PostConstruct
	public void init(){
		artists.put("1",createArtist("1", "john"));
		artists.put("2",createArtist("2", "anne"));
		artists.put("3",createArtist("3", "david"));
		artists.put("4",createArtist("4", "sabrina"));
	}
	
	@Override
	public Iterable<Artist> findAll() {
		return artists.values();
	}

	@Override
	public Artist findOne(String id) {
		return artists.get(id);
	}

	@Override
	public Artist delete(String id) {
		return artists.remove(id);
	}

	@Override
	public void save(Artist artist) {		
		artists.put(artist.getId(), artist);
	}
	
	private Artist createArtist(String id, String name){
		Artist artist = new Artist();
		artist.setId(id);
		artist.setName(name);
		return artist;
	}
	
}
