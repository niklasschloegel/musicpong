package business;

import data.Playlist;
import data.Track;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

	private final Object skipMonitor;
	private int pos = 0;
	private Minim minim;
	private Playlist currentPlaylist, originalPlaylist;
	private float volume = 1;
	private int currentTrackNumber = 0;
	private boolean shuffle = false, looping = false, changed = false;
	private SimpleBooleanProperty playing = new SimpleBooleanProperty(this, "playing", false);
	private SimpleBooleanProperty boom = new SimpleBooleanProperty(false);
	private boolean originalPlaylistIsset = false;
	private SimpleObjectProperty<Track> track;
	private Thread autochange;
	private BeatDetect beat;
	private AudioPlayer song;

	public MP3Player() {
		minim = new SimpleMinim();
		skipMonitor = new Object();
		track = new SimpleObjectProperty<Track>();
	}

	public SimpleObjectProperty<Track> trackPropProperty() {
		return track;
	}

	public void play(String filename) {
		if (song != null)
			pause();
		playing.set(true);
		song = minim.loadFile(filename, 2048);
		play();
		track.set(currentPlaylist.getTrack(currentTrackNumber));
		/* Beat detect thread */
		new Thread() {
			@Override
			public void run() {
				beat = new BeatDetect(song.bufferSize(), song.sampleRate());
				beat.setSensitivity(500);
				//System.out.println("BEAT DETECT \n");
				while (isPlaying()) {
					beat.detect(song.mix);
					/*
					 * if(beat.isHat()) { System.out.print("HAT \n"); }
					 */
					/*
					 * if(beat.isSnare()) { System.out.print("SNARE \n"); }
					 */
//					beat.isRange(arg0, arg1, arg2)
					if (beat.isKick()) {
						boom.set(true);
					}
					boom.set(false);
				}
			}
		}.start();
	}

	public void play() {
		if (song != null) {
			new Thread(() -> {
				playing.set(true);
				setVolume(volume);
				song.play(pos);
			}).start();
		}
		autochange = new Thread() {
			public void run() {
				while ((currentPlaylist.getTrack(currentTrackNumber).getSliderLength() / 100) != (getSliderTime() / 100)
						&& !isInterrupted()) {
					try {
						sleep(10);
					} catch (InterruptedException e) {
						interrupt();
					}
				}
				if (playing.get() && !isInterrupted()) {
					if (looping) {
						song.rewind();
						interrupt();
						play();
					} else {
						skip();
					}
				}
			}
		};
		 autochange.start();
	}

	public void pause() {
		if (song != null) {
			playing.set(false);
			autochange.interrupt();
			song.pause();
			pos = song.position();
		}
	}

	public void stop() {
		if (song != null) {
			playing.set(false);
			autochange.interrupt();
			song.rewind();
			song.pause();
		}
	}

	public void mute() {
		if (song.isMuted()) {
			song.unmute();
		} else {
			song.mute();
		}
	}

	public boolean isMuted() {
		return song.isMuted();
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float value) {
		volume = value;
		if (song != null) {
			if (value >= 0 && value <= 1) {
				float tmp = (float) (10 * Math.log(value));
				song.setGain(tmp);
			}
		}
	}

	public int getCurTime() {
		return (song.position() / 1000);
	}

	public void setCurTime(int i) {
		int time = i * 1000;
		song.cue(time);
	}

	public int getSliderTime() {
		return (song.position());
	}

	public void setStartTime(int i) {
		int time = i * 1000;
		pos = time;
	}

	public void setPlaylist(Playlist playlist) {
		currentPlaylist = playlist;
		changed = true;
		currentTrackNumber = 0;
	}

	public synchronized void setPlaylist(Playlist playlist, int currentTrackNumber) {
		this.currentTrackNumber = currentTrackNumber;
		currentPlaylist = playlist;
		changed = true;
	}

	public Playlist getDefaultPlaylist() {
		return PlaylistManager.getDefaultPlaylist();
	}

	public void skip() {
		stop();
		synchronized (skipMonitor) {
			currentTrackNumber++;
			if (currentTrackNumber >= currentPlaylist.numberOfTracks())
				currentTrackNumber = 0;
			play(currentPlaylist.getTrack(currentTrackNumber).getSoundFile());
		}
	}

	public void skipBack() {
		stop();
		synchronized (skipMonitor) {
			currentTrackNumber--;
			if (currentTrackNumber < 0)
				currentTrackNumber = currentPlaylist.numberOfTracks() - 1;
			play(currentPlaylist.getTrack(currentTrackNumber).getSoundFile());
		}
	}

	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	public void shuffle() {
		if (!originalPlaylistIsset) {
			originalPlaylist = currentPlaylist;
			originalPlaylistIsset = true;
		}

		if (shuffle) {
			setShuffle(false);
			setPlaylist(originalPlaylist, originalPlaylist.getTrackIndex(getCurrentTrack()));
			originalPlaylistIsset = false;
		} else {
			setShuffle(true);
			Playlist shuffled = originalPlaylist.createShuffle(getCurrentTrack());
			setPlaylist(shuffled);
		}

	}

	public void repeat() {
		looping = !looping;
	}

	public boolean isPlaying() {
		return playing.get();
	}

	public boolean isLooping() {
		return looping;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public Playlist getCurPlaylist() {
		return currentPlaylist;
	}

	public int getCurTrackNumber() {
		return currentTrackNumber;
	}

	public void setCurTrackNumber(int i) {
		currentTrackNumber = i;
	}

	public Track getCurrentTrack() {
		return getCurPlaylist().getTrack(getCurTrackNumber());
	}

	public SimpleBooleanProperty playingProperty() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing.set(playing);
	}

	public boolean isBoom() {
		return boom.get();
	}

	public SimpleBooleanProperty boomProperty() {
		return boom;
	}
}