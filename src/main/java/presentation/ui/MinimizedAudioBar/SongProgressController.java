package presentation.ui.MinimizedAudioBar;

import business.MP3Player;
import data.Track;
import javafx.application.Platform;
import presentation.FXMain;
import presentation.ui.AbstractViewController;

import java.util.Timer;
import java.util.TimerTask;

public class SongProgressController extends AbstractViewController<FXMain> {

	public SongProgressController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		rootView = new SongProgressView();
		initialize();
	}

	@Override
	public void initialize() {
		mp3Player.trackPropProperty().addListener((ov) -> {
			initTrack();
		});
	}

	private void initTrack() {
		Track t = mp3Player.getCurrentTrack();
		Platform.runLater(() -> {
			((SongProgressView) rootView).curTime.setText("0:00");
			((SongProgressView) rootView).maxTime.setText(t.getLengthString());
			((SongProgressView) rootView).progressBar.setProgress(0);
		});
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						((SongProgressView) rootView).curTime.setText(getTime(mp3Player.getCurTime()));
						((SongProgressView) rootView).progressBar.setProgress(((double) mp3Player.getCurTime()) / ((double) mp3Player.getCurrentTrack().getLength()));
					}
				});
			}
		}, 0, 10);
	}
	
	// ZEITUMRECHNUNG
	private String getTime(int i) {
		int min = i / 60;
		int sek = i - (60 * min);
		return String.format("%d:%02d", min, sek);
	}
}
