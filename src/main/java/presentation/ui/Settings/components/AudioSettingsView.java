package presentation.ui.Settings.components;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import presentation.utils.UnsupportedRegionException;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.*;

class AudioSettingsView extends BorderPane {

	StackPane animationPane;
	HBox importBox, successBox, backBox;
	VBox loadingBox;
	Button easy, middle, hard, importButton;
	ProgressBar progress;

	AudioSettingsView() {
		super();

		VBox topWrapper = new VBox();
		backBox = new HBox();
		Button back = new Button();
		back.setId("backButton");
		back.setPickOnBounds(true);
		Label backLabel = new Label();
		try{
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, backLabel);
			applyColorsTo(UIColor.SECONDARY_COLOR, CSSAttribute.BG_COLOR, back);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		initLanguage(backLabel, "back");
		backBox.getChildren().addAll(back, backLabel);
		backBox.setSpacing(6);
		backBox.setAlignment(Pos.CENTER_LEFT);

		// Drag/Drop / Import songs part
		importBox = new HBox();

		VBox dragPart = new VBox();
		Button importIcon = new Button();
		importIcon.setId("import-icon");
		try {
			applyColorsTo(UIColor.BG_COLOR_DARK, CSSAttribute.BG_COLOR, importIcon);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		Label dropHint = new Label();
		initLanguage(dropHint, "settings.dropHint");
		try {
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, dropHint);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		dragPart.getChildren().addAll(importIcon, dropHint);
		dragPart.setAlignment(Pos.CENTER);
		dragPart.setSpacing(5);

		Separator separator = new Separator();
		separator.setPrefHeight(72);
		separator.setOrientation(Orientation.VERTICAL);
		separator.getStyleClass().add("separator");
		try {
			applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.BG_COLOR, separator);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		importButton = new Button();
		initLanguage(importButton, "settings.importSongs");
		importButton.setAlignment(Pos.CENTER);
		try {
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, importButton);
			applyColorsTo(UIColor.BG_COLOR_DARK, CSSAttribute.BG_COLOR, importButton);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		importBox.getChildren().addAll(dragPart, separator, importButton);
		importBox.setAlignment(Pos.CENTER);
		importBox.setId("import-box");
		try {
			applyColorsTo(UIColor.SECONDARY_COLOR_DARK, CSSAttribute.BG_COLOR, importBox);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		setMargin(importBox, new Insets(60));
		importBox.setSpacing(20);
		importBox.setPrefHeight(80);

		loadingBox = new VBox();
		progress = new ProgressBar();
		progress.setProgress(0);
		Label loading = new Label();
		initLanguage(loading, "settings.loading");
		loadingBox.setSpacing(8);
		loadingBox.setAlignment(Pos.CENTER);

		successBox = new HBox();
		Button successIcon = new Button();
		successIcon.setId("successIcon");
		Label success = new Label();
		initLanguage(success, "settings.success");
		successBox.setSpacing(8);
		successBox.setAlignment(Pos.CENTER);

		try {
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, success, loading);
			applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, progress);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}


		loadingBox.getChildren().addAll(loading, progress);
		loadingBox.setPadding(new Insets(10));
		successBox.getChildren().addAll(successIcon, success);
		successBox.setOpacity(0);
		animationPane = new StackPane();
		animationPane.getChildren().addAll(loadingBox,successBox);


		// Playlist Nodes
		VBox btnContainer = new VBox();

		easy = new Button();
		middle = new Button();
		hard = new Button();

		try {
			String path = Paths.get(System.getProperty("user.dir") + "/assets/images/defaultCover.png").toUri().toURL()
					.toString();
			ImageView playlistImg = new ImageView(path);
			ImageView playlistImg2 = new ImageView(path);
			ImageView playlistImg3 = new ImageView(path);

			playlistImg.setFitWidth(70);
			playlistImg.setFitHeight(70);
			playlistImg2.setFitWidth(70);
			playlistImg2.setFitHeight(70);
			playlistImg3.setFitWidth(70);
			playlistImg3.setFitHeight(70);

			easy.setGraphic(playlistImg);
			middle.setGraphic(playlistImg2);
			hard.setGraphic(playlistImg3);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		easy.setGraphicTextGap(30);
		middle.setGraphicTextGap(30);
		hard.setGraphicTextGap(30);

		initLanguage(easy, "easy");
		initLanguage(middle, "normal");
		initLanguage(hard, "hard");
		addCSSClassTo("playlistButtons", easy, middle, hard);
		try {
			applyColorsTo(UIColor.BG_COLOR_DARK, CSSAttribute.TEXT_FILL, easy, middle, hard);
			applyColorsTo(UIColor.MAIN_COLOR_DARK, CSSAttribute.BG_COLOR, easy, middle, hard);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		btnContainer.getChildren().addAll(animationPane, easy, middle, hard);
		btnContainer.setSpacing(20);
		btnContainer.setAlignment(Pos.CENTER);

		topWrapper.setSpacing(20);
		topWrapper.getChildren().addAll(backBox, importBox);
		setTop(topWrapper);
		setCenter(btnContainer);
		setPadding(new Insets(10));
		try {
			applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, this);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
	}
}
