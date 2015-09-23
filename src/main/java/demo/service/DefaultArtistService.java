package demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import demo.model.Artist;
import demo.repository.ArtistRepository;

@Component
public class DefaultArtistService implements ArtistService {

	@Autowired
	private ArtistRepository repository;
	
	@Override
	public Iterable<Artist> findAll() {		
		return repository.findAll();
	}

	@Override
	public Artist findOne(String id) {
		return repository.findOne(id);
	}

	@Override
	public Artist delete(String id) {
		return repository.delete(id);
	}

	@Override
	public void save(Artist artist) {
		if (artist.getId() == null){
			artist.setId(UUID.randomUUID().toString());
		}
		repository.save(artist);
	}

}
