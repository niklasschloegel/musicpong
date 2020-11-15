package presentation.ui.AudioPlayBar.components;

import business.MP3Player;
import data.Track;
import javafx.application.Platform;
import javafx.scene.image.Image;
import presentation.FXMain;
import presentation.ui.AbstractViewController;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class SongInfoController extends AbstractViewController<FXMain> {

	public SongInfoController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		rootView = new SongInfoView();

		initialize();
	}

	@Override
	public void initialize() {

		mp3Player.trackPropProperty().addListener((observableValue, track, t1) -> {
			refreshSongInfo();
		});
	}

	private void refreshSongInfo() {
		Track t = mp3Player.getCurrentTrack();
		Platform.runLater(() -> {
			((SongInfoView) rootView).title.setText(t.getTitle());
			((SongInfoView) rootView).artist.setText(t.getArtist());
			((SongInfoView) rootView).album.setText(t.getAlbumTitle());
			Image img = null;
			if (t.getImage() == null || t.getImage().length <= 0) {
				try {
					img = new Image(new FileInputStream(System.getProperty("user.dir") + "/assets/images/defaultCover.png"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				img = new Image(new ByteArrayInputStream(t.getImage()));
			}
			((SongInfoView) rootView).cover.setImage(img);
		});
	}
}
