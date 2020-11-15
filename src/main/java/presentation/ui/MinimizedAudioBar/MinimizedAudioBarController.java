package presentation.ui.MinimizedAudioBar;

import business.MP3Player;
import javafx.scene.layout.HBox;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.AudioPlayBar.components.SongInfoController;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.*;

public class MinimizedAudioBarController extends AbstractViewController<FXMain> {

	SongProgressController songProgressController;
	SongInfoController songInfoController;

	public MinimizedAudioBarController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);

		rootView = new HBox();

		songInfoController = new SongInfoController(application, mp3Player);
		songProgressController = new SongProgressController(application, mp3Player);

		rootView.getChildren().addAll(songInfoController.getRootView(), songProgressController.getRootView());
		rootView.setId("minimized-bar");

		try {
			applyColorsTo(UIColor.BG_COLOR_DARK, CSSAttribute.BG_COLOR, rootView);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		initialize();
	}

	@Override
	public void initialize() {
		application.getStage().widthProperty().addListener(e -> {
			songInfoController.getRootView().setPrefWidth(application.getStage().getWidth() * 1 / 3);
			songProgressController.getRootView().setPrefWidth(application.getStage().getWidth() * 2 / 3);
			((SongProgressView) songProgressController.getRootView()).progressBar
					.setPrefWidth(application.getStage().getWidth() * 0.5);
		});

	}
}
