package presentation.ui.AudioPlayBar.components;

import business.MP3Player;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import presentation.FXMain;
import presentation.ui.AbstractViewController;

public class VolumeSliderController extends AbstractViewController<FXMain> {

	private Slider volumeSlider;
	private Button volumeIcon;
	private double oldVolume;

	public VolumeSliderController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		rootView = new VolumeSliderView();
		volumeSlider = ((VolumeSliderView) rootView).volumeSlider;
		volumeIcon = ((VolumeSliderView) rootView).volumeIcon;
		oldVolume = 0;
		initialize();
	}

	@Override
	public void initialize() {

		volumeIcon.setOnAction(e -> {
			if (mp3Player.isMuted()) {
				volumeSlider.setValue(oldVolume);

			} else {
				oldVolume = volumeSlider.getValue();
				volumeSlider.setValue(0);
			}
			mp3Player.mute();

		});

		volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			mp3Player.setVolume(newValue.floatValue());
			if (newValue.doubleValue() <= 1.0 && newValue.doubleValue() > 0.5) {
				volumeIcon.setId("volume2");
			} else if (newValue.doubleValue() <= 0.5 && newValue.doubleValue() > 0.01) {
				volumeIcon.setId("volume1");
			} else {
				volumeIcon.setId("volume0");
			}
		});
	}
}
