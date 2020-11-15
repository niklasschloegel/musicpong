package data;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Playlist implements Iterable<Track> {

	private List<Track> tracks;
	private String title;

	public Playlist() {
		tracks = new LinkedList<>();
	}

	public Playlist(String title) {
		this.title = title;
		tracks = new LinkedList<>();
	}

	private Playlist(Playlist original) {
		this(original.getTitle());

		for (Track t : original) {
			this.add(t);
		}
	}

	public void add(Track track) {
		tracks.add(track);
	}

	public void add(int index, Track track) {
		tracks.add(index, track);
	}

	public Playlist createShuffle(Track currentlyPlaying) {
		Playlist shuffled = new Playlist(this);
		Collections.shuffle(shuffled.tracks);
		shuffled.remove(currentlyPlaying);
		shuffled.add(0, currentlyPlaying);
		return shuffled;
	}

	public Track getTrack(int number) {
		return tracks.get(number);
	}

	public int numberOfTracks() {
		return tracks.size();
	}

	public int getTrackNumber(String s) {
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i).getTitle().equals(s)) {
				return i;
			}
		}
		return 0;
	}

	public int getTrackIndex(Track t) {
		return tracks.indexOf(t);
	}

	public int getSize() {
		return tracks.size();
	}

	public String getLengthString() {
		long seconds = tracks.stream().mapToLong(Track::getLength).sum();
		long hours = TimeUnit.SECONDS.toHours(seconds);

		seconds -= TimeUnit.HOURS.toSeconds(hours);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds);
		if (hours>0) {
			return String.format("%dh %dmin", hours, minutes);
		} else {
			return String.format("%dmin", minutes);
		}
	}


	public void remove(Track t) {
		tracks.remove(t);
	}

	public boolean isEmpty() {
		return tracks.size() == 0;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public Iterator<Track> iterator() {
		return tracks.iterator();
	}

}