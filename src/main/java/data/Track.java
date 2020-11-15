package data;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Track {

	private boolean spotified;
    private Mp3File file;
	private ID3v2 tag;
	private String soundFile, title, artist, albumTitle;
	private int length, sliderLength;
	private Float bpm;
	private byte[] image;
	
	public Track(String soundFile) {
		this.soundFile = soundFile;
		loadTagInformation();
	}

	@Override
	public String toString(){
		return String.format("%s - %s", artist, title);
	}
	
	public void loadTagInformation() {
		try {
		    file = new Mp3File(soundFile);
			tag = file.getId3v2Tag();
			title = tag.getTitle();
			albumTitle = tag.getAlbum();
			artist = tag.getArtist();
			image = tag.getAlbumImage();
			sliderLength = (int) file.getLengthInMilliseconds();
			length = (int) file.getLengthInSeconds();
			bpm = (float) tag.getBPM();
			assert bpm != null;
			if (bpm <= 0) {
				try {
					bpm = Beat.getSpotifysBPM(this);
					spotified = true;
				} catch (SpotifyNotFoundException e) {
					bpm = Beat.getAlternativeBPM(this);
					spotified = false;
				}
			}

		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getSoundFile(){
		return soundFile;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public String getAlbumTitle(){
		return albumTitle;
	}

	public int getLength() {
		return length;
	}
	
	public int getSliderLength() {
		return sliderLength;
	}

	public byte[] getImage() {
		return image;
	}

	public float getBPM() {
		return bpm;
	}

	public boolean isSpotified() {
		return spotified;
	}

	public String getLengthString(){
        return getLengthString(getLength());
    }

    public static String getLengthString(int seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        long restSeconds = seconds - minutes*60;
        return String.format("%d:%02d", minutes, restSeconds);
    }
}