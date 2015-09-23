package demo.service;

import demo.model.Artist;

/**
 * 
 * Defines operations on {@link Artist}
 *
 */
public interface ArtistService {
	/**
	 * Find all artists.
	 * @return all artists
	 */
	public Iterable<Artist> findAll();

	/**
	 * Finds artist by id.
	 * 
	 * @param id
	 *            artist id
	 * @return {@link Artist} object
	 */
	public Artist findOne(String id);

	/**
	 * Removes artist by id.
	 * 
	 * @param id
	 *            artist id
	 * @return deleted {@link Artist} object
	 */
	public Artist delete(String id);

	/**
	 * Saves artist.
	 * 
	 * @param artist
	 *            artist to be saved
	 */
	public void save(Artist artist);
}
