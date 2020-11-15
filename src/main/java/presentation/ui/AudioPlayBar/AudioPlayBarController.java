package presentation.ui.AudioPlayBar;

import business.MP3Player;
import javafx.scene.layout.BorderPane;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.AudioPlayBar.components.AudioControlController;
import presentation.ui.AudioPlayBar.components.SongInfoController;
import presentation.ui.AudioPlayBar.components.VolumeSliderController;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;
import static presentation.utils.StyleAssist.applyColorsTo;

public class AudioPlayBarController extends AbstractViewController<FXMain> {

	private SongInfoController songInfo;
	private AudioControlController audioControls;
	private VolumeSliderController volumeSliderView;

	public AudioPlayBarController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);

		rootView = new BorderPane();
		songInfo = new SongInfoController(application, mp3Player);
		audioControls = new AudioControlController(application, mp3Player);
		volumeSliderView = new VolumeSliderController(application, mp3Player);

		try {
			applyColorsTo(StyleAssist.UIColor.BG_COLOR_DARK, StyleAssist.CSSAttribute.BG_COLOR, rootView);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		rootView.setId("audioPlayBar");

		((BorderPane) rootView).setLeft(songInfo.getRootView());
		((BorderPane) rootView).setCenter(audioControls.getRootView());
		((BorderPane) rootView).setRight(volumeSliderView.getRootView());

		songInfo.getRootView().prefWidthProperty().bind(application.getStage().widthProperty().multiply(1 / 3));
		audioControls.getRootView().prefWidthProperty().bind(application.getStage().widthProperty().multiply(1 / 3));
		volumeSliderView.getRootView().prefWidthProperty().bind(application.getStage().widthProperty().multiply(1 / 3));

		initialize();
	}

	@Override
	public void initialize() {

		application.getStage().widthProperty().addListener(e -> {
			songInfo.getRootView().setMinWidth(application.getStage().widthProperty().get() / 4);
			volumeSliderView.getRootView().setMinWidth(application.getStage().widthProperty().get() / 4);
		});

	}

}
