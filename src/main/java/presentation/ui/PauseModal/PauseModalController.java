package presentation.ui.PauseModal;

import business.MP3Player;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.AudioPlayBar.AudioPlayBarController;
import presentation.ui.Scenes;
import presentation.ui.YesNoPopupView.YesNoPopupController;
import presentation.utils.PopUpAssist;

public class PauseModalController extends AbstractViewController<FXMain> {

	private Button resume, settings, exit;
	private SimpleBooleanProperty resumeTriggered, settingsTriggered, wantsToEndGame;
	private YesNoPopupController yesNoPopupController;
	private Stage really;

	public PauseModalController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);

		rootView = new PauseModal();
		((PauseModal) rootView).setBottom(new AudioPlayBarController(application, mp3Player).getRootView());

		resume = ((PauseModal) rootView).resume;
		settings = ((PauseModal) rootView).settings;
		exit = ((PauseModal) rootView).exit;
		resumeTriggered = new SimpleBooleanProperty(false);
		settingsTriggered = new SimpleBooleanProperty(false);
		wantsToEndGame = new SimpleBooleanProperty(false);

		yesNoPopupController = new YesNoPopupController(application, "exit.question");
		really = PopUpAssist.createPopUp(yesNoPopupController.getRootView() , application.getStage());

		initialize();
	}

	@Override
	public void initialize() {

		resume.setOnAction(e->{
			resumeTriggered.set(true);
			resumeTriggered.set(false);
		});

		settings.setOnAction(e->{
			application.switchScene(Scenes.SETTINGS_VIEW, Scenes.PONG_VIEW);
			settingsTriggered.set(true);
			settingsTriggered.set(false);
		});

		exit.setOnAction(e->{
			PopUpAssist.center(really, application.getStage());
			really.show();
		});

		yesNoPopupController.yesTriggerProperty().addListener((observableValue, aBoolean, t1) -> {
			really.close();
			wantsToEndGame.set(true);
			wantsToEndGame.set(false);
		});

		yesNoPopupController.noTriggerProperty().addListener((observableValue, aBoolean, t1) -> {
			really.close();
			wantsToEndGame.set(false);
		});
	}

	public SimpleBooleanProperty resumeTriggeredProperty() {
		return resumeTriggered;
	}

	public SimpleBooleanProperty settingsTriggeredProperty() {
		return settingsTriggered;
	}

	public SimpleBooleanProperty wantsToEndGameProperty() {
		return wantsToEndGame;
	}
}