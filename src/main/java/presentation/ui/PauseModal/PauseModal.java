package presentation.ui.PauseModal;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.*;

class PauseModal extends BorderPane {

	Button resume, settings, exit;

	PauseModal() {
		super();
		VBox pauseModal = new VBox();
		Label pause = new Label();

		HBox box = new HBox();
		resume = new Button();
		settings = new Button();
		exit = new Button();

		initLanguage(pause, "pauseMenu.pause");
		initLanguage(resume, "pauseMenu.resume");
		initLanguage(settings, "pauseMenu.settings");
		initLanguage(exit, "pauseMenu.exit");
		pause.setId("pause-label");
		addCSSClassTo("pause-buttons", resume, settings, exit);

		try {
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, pause, resume, settings, exit);
			applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, resume, settings, exit);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		box.setAlignment(Pos.CENTER);
		try {
			applyColorsTo(UIColor.SECONDARY_COLOR, CSSAttribute.BG_COLOR, box);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		box.setId("pause-box");
		box.getChildren().addAll(resume, settings, exit);

		pauseModal.setAlignment(Pos.CENTER);
		pauseModal.getChildren().addAll(pause, box);
		pauseModal.setId("pause-modal");

		setCenter(pauseModal);

	}
}
