package presentation.ui.Settings.components;

import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import presentation.utils.LanguageAssist;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

import java.util.Arrays;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.applyColorsTo;

class SettingsView extends BorderPane {

    ToggleButton hudBtn;
	ComboBox<LanguageAssist.SupportedLanguage> languageBox;
	ComboBox<Integer> fpsBox;

	SettingsView() {
		super();

		//Header
		Label header = new Label();
		header.setId("settings-header");
		initLanguage(header, "settings.header");

		//Hud
        Label hudLabel = new Label();
        initLanguage(hudLabel, "settings.hud");
        hudBtn = new JFXToggleButton();

        //Language
		Label languageLabel = new Label();
		initLanguage(languageLabel, "settings.language");
		languageBox = new ComboBox<>();
		languageBox.setItems(LanguageAssist.SupportedLanguage.getAllSupportedLanguages());

		//FPS
		Label fpsLabel = new Label();
		initLanguage(fpsLabel, "settings.fps");
		fpsBox = new ComboBox<>();
		fpsBox.setItems(FXCollections.observableList(Arrays.asList(30, 60, 120)));


		//Credentials
		HBox credentials = new HBox();
		Label credentialText = new Label();
		initLanguage(credentialText, "credentials");
		credentials.getChildren().add(credentialText);
		credentialText.setAlignment(Pos.CENTER);
		credentials.setAlignment(Pos.CENTER);


		try {
			applyColorsTo(StyleAssist.UIColor.BG_COLOR, StyleAssist.CSSAttribute.BG_COLOR, languageBox, hudBtn, fpsBox);
            applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, header, hudLabel, languageLabel, fpsLabel, credentialText);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(75);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);

		GridPane grid = new GridPane();
		grid.setHgap(30);
		grid.getColumnConstraints().addAll(col1, col2);
		grid.add(header, 0, 0, 2, 1);
		grid.add(hudLabel, 0, 2);
		grid.add(hudBtn, 1, 2);
		grid.add(languageLabel, 0, 3);
		grid.add(languageBox, 1, 3);
		grid.add(fpsLabel, 0, 5);
		grid.add(fpsBox, 1, 5);
		grid.setPadding(new Insets(20));

		setTop(grid);
		setBottom(credentials);
	}

}
