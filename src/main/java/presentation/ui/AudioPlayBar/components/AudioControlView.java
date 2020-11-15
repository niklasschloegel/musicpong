package presentation.ui.AudioPlayBar.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.addCSSClassTo;
import static presentation.utils.StyleAssist.applyColorsTo;

class AudioControlView extends VBox {

    Button loop, skipBack, playPause, skip, shuffle;

	AudioControlView() {
		super();
		HBox audioButtons = new HBox();

		loop = new Button();
		skipBack = new Button();
		playPause = new Button();
		skip = new Button();
		shuffle = new Button();

		loop.setId("loop");
		loop.setPickOnBounds(true);
		skipBack.setId("skipBack");
		skipBack.setPickOnBounds(true);
		playPause.setId("play");
		playPause.setPickOnBounds(true);
		skip.setId("skip");
		skip.setPickOnBounds(true);
		shuffle.setId("shuffle");
		shuffle.setPickOnBounds(true);

		try {
			applyColorsTo(StyleAssist.UIColor.MAIN_COLOR, StyleAssist.CSSAttribute.BG_COLOR, skipBack, playPause, skip);
			applyColorsTo(StyleAssist.UIColor.TEXT_COLOR_DARK, StyleAssist.CSSAttribute.BG_COLOR, loop, shuffle);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		addCSSClassTo("audioMainButtons", skipBack, playPause, skip);
		addCSSClassTo("audioSecondaryButtons", loop, shuffle);

		audioButtons.getChildren().addAll(loop, skipBack, playPause, skip, shuffle);

		audioButtons.setAlignment(Pos.CENTER);
		audioButtons.setSpacing(30);

		getChildren().addAll(audioButtons);
		setSpacing(12);
		setAlignment(Pos.CENTER);
	}

}