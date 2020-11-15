package presentation.ui.AudioPlayBar.components;

import business.MP3Player;
import javafx.scene.layout.VBox;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.MinimizedAudioBar.SongProgressController;
import presentation.utils.UnsupportedRegionException;
import static presentation.utils.StyleAssist.*;

public class AudioControlController extends AbstractViewController<FXMain> {

	AudioControlView control;
	
	public AudioControlController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		SongProgressController progress = new SongProgressController(application, mp3Player);
		control = new AudioControlView();
		rootView = new VBox();
		((VBox)rootView).setSpacing(12);
		rootView.getChildren().addAll(control, progress.getRootView());
		initialize();
	}

	@Override
	public void initialize() {
		// TODO: track slider to progressbar
		// TODO: add missing listeners to audio buttons

		mp3Player.playingProperty().addListener((observableValue, aBoolean, t1) -> {
			if (t1) {
				control.playPause.setId("pause");
			} else {
				control.playPause.setId("play");
			}
		});

		control.playPause.setOnAction(e -> {
			if (mp3Player.isPlaying()) {
				mp3Player.pause();
			} else {
				mp3Player.play();
			}
		});

		control.skip.setOnAction(e -> mp3Player.skip());

		control.skipBack.setOnAction(e -> mp3Player.skipBack());

		control.shuffle.setOnAction(e -> {
			unstyleRegion(control.shuffle);
			if (mp3Player.isShuffle()) {
				try {
					applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.BG_COLOR,
							control.shuffle);
				} catch (UnsupportedRegionException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					applyColorsTo(UIColor.MAIN_COLOR_DARK, CSSAttribute.BG_COLOR,
							control.shuffle);
				} catch (UnsupportedRegionException e1) {
					e1.printStackTrace();
				}
			}
			mp3Player.shuffle();
		});
		
		control.loop.setOnAction(e -> {
			unstyleRegion(control.loop);
			if (mp3Player.isLooping()) {
				try {
					applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.BG_COLOR,
							control.loop);
				} catch (UnsupportedRegionException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					applyColorsTo(UIColor.MAIN_COLOR_DARK, CSSAttribute.BG_COLOR,
							control.loop);
				} catch (UnsupportedRegionException e1) {
					e1.printStackTrace();
				}
			}
			mp3Player.repeat();
		});
	}
}