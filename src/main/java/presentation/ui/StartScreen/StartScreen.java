package presentation.ui.StartScreen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import presentation.utils.LanguageAssist;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

public class StartScreen extends BorderPane {
	
	Button play, easy, normal, hard, settings;
	StackPane startPane;
	private VBox imagePane;
	private ImageView imageView;
	HBox pane1, pane2;
	
	StartScreen() {
		super();
		VBox startScreen = new VBox();
		
		settings = new Button();
		settings.setId("start-settings");
		
		pane1 = new HBox();
		play = new Button();
		play.setId("start-play");
		
		pane1.setAlignment(Pos.CENTER);
		pane1.getChildren().addAll(play);
		
		pane2 = new HBox();
		easy = new Button();
		easy.setId("start-easy");
		LanguageAssist.initLanguage(easy, "easy");
		normal = new Button();
		normal.setId("start-normal");
		LanguageAssist.initLanguage(normal, "normal");
		hard = new Button();
		hard.setId("start-hard");
		LanguageAssist.initLanguage(hard, "hard");
		pane2.setAlignment(Pos.CENTER);
		pane2.setMinHeight(100);
		pane2.setSpacing(16);
		pane2.getChildren().addAll(easy,normal,hard);
		
		
		startPane = new StackPane();
		startPane.setAlignment(Pos.CENTER);
		pane2.setOpacity(0);
		startPane.getChildren().addAll(pane2,pane1);
		
		imagePane = new VBox();
		imageView = new ImageView("file:assets/images/logo.png");
		imageView.setFitHeight(360);
		imageView.setFitWidth(640);
		imagePane.getChildren().addAll(imageView);
		imagePane.setAlignment(Pos.CENTER);

		try {
			StyleAssist.applyColorsTo(StyleAssist.UIColor.BG_COLOR, StyleAssist.CSSAttribute.BG_COLOR, startScreen);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		startScreen.getChildren().addAll(settings,imagePane,startPane);

		setCenter(startScreen);	
	}	
}