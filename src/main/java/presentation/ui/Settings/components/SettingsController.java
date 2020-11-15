package presentation.ui.Settings.components;

import business.MP3Player;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.Pong.PongViewController;
import presentation.utils.LanguageAssist;

public class SettingsController extends AbstractViewController<FXMain> {

	ToggleButton hudBtn;
	ComboBox<LanguageAssist.SupportedLanguage> languageBox;
	ComboBox<Integer> fpsBox;

	public SettingsController(FXMain application, MP3Player mp3Player) {
		super(application, mp3Player);
		rootView = new SettingsView();

//        if (StyleAssist.isDarkMode()) {
//            ((SettingsView) rootView).darkModeBtn.setSelected(true);
//        }
		hudBtn = ((SettingsView) rootView).hudBtn;
		hudBtn.setSelected(PongViewController.isHudShow());

		languageBox = ((SettingsView) rootView).languageBox;
		languageBox.setValue(LanguageAssist.getCurrentLanguage());

		fpsBox = ((SettingsView) rootView).fpsBox;
		fpsBox.setValue(PongViewController.getFps());

		initialize();
	}

	@Override
	public void initialize() {

		hudBtn.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
			PongViewController.setHudShow(t1);
		});

		languageBox.valueProperty().addListener((observableValue, supportedLanguage, t1) -> {
			LanguageAssist.loadLanguage(t1);
		});

		fpsBox.valueProperty().addListener((observableValue, integer, t1) -> {
			PongViewController.setFps(t1);
		});

	}
}
