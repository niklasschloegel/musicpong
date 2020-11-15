package presentation.ui.StartScreen;

import business.MP3Player;
import business.PlaylistManager;
import data.Playlist;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.Scenes;
import presentation.ui.StartScreen.Warning.WarningController;
import presentation.utils.LanguageAssist;
import presentation.utils.PopUpAssist;
import static presentation.utils.FadeAssist.crossFade;

public class StartScreenController extends AbstractViewController<FXMain> {

	private Button play, easy, normal, hard, settings;
	private HBox pane1, pane2;
	private StackPane startPane;

	public StartScreenController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		rootView = new StartScreen();
		play = ((StartScreen) rootView).play;
		easy = ((StartScreen) rootView).easy;
		normal = ((StartScreen) rootView).normal;
		hard = ((StartScreen) rootView).hard;
		settings = ((StartScreen) rootView).settings;
		pane1 = ((StartScreen) rootView).pane1;
		pane2 = ((StartScreen) rootView).pane2;
		startPane = ((StartScreen) rootView).startPane;
		initialize();
	}

	@Override
	public void initialize() {
		play.setOnAction(event -> {
			crossFade(pane1,pane2);
			startPane.getChildren().remove(pane1);
		});

		easy.setOnAction(event -> startGame(PlaylistManager.EASY_PLAYLIST));
		normal.setOnAction(event -> startGame(PlaylistManager.MIDDLE_PLAYLIST));
		hard.setOnAction(event -> startGame(PlaylistManager.HARD_PLAYLIST));

		settings.setOnAction(event -> application.switchScene(Scenes.SETTINGS_VIEW, Scenes.HOME_VIEW));
	}

	private void showWarning(Playlist playlist) {
		String msg = LanguageAssist.getValue("warning.emptyPlaylist") + " " + LanguageAssist.getValue(playlist.getTitle());
		WarningController wc = new WarningController(application, msg);

		Stage popup = PopUpAssist.createPopUp(wc.getRootView(), application.getStage());
		wc.wantsToGetClosedProperty().addListener((observableValue, aBoolean, t1) -> {
			if (t1) {
				popup.close();
			}
		});

		popup.setTitle(LanguageAssist.getValue("warning"));
		PopUpAssist.center(popup, application.getStage());
		popup.show();

	}

	private void startGame(Playlist playlist) {
		if (playlist.getSize() < 1) {
			showWarning(playlist);
		} else {
			application.switchScene(Scenes.PONG_VIEW, Scenes.HOME_VIEW);
			mp3Player.stop();
			mp3Player.setPlaylist(playlist);
			mp3Player.play(mp3Player.getCurPlaylist().getTrack(0).getSoundFile());
			startPane.getChildren().add(pane1);
			pane1.setOpacity(1);
			pane2.setOpacity(0);
		}
	}


}
