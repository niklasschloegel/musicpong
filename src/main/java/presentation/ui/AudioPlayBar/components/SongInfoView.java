package presentation.ui.AudioPlayBar.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import static presentation.utils.StyleAssist.applyColorsTo;

class SongInfoView extends VBox {

	Label title, artist, album;
	ImageView cover;

	SongInfoView() {
		super();

		GridPane grid = new GridPane();

		try {
			cover = new ImageView(Paths.get(System.getProperty("user.dir") + "assets/images/defaultCover.png").toUri()
					.toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		cover.setFitWidth(55);
		cover.setFitHeight(55);

		VBox songInfo = new VBox();
		title = new Label();
		artist = new Label();
		album = new Label();

		try {
			applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, title, artist, album);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		songInfo.getChildren().addAll(title, artist, album);
		songInfo.setAlignment(Pos.CENTER_LEFT);
		songInfo.setPadding(new Insets(10));

		grid.add(cover, 0, 0);
		grid.add(songInfo, 1, 0);

		getChildren().add(grid);
		setAlignment(Pos.CENTER);
	}

}
