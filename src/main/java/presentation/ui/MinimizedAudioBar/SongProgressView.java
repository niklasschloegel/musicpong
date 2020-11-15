package presentation.ui.MinimizedAudioBar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.*;

class SongProgressView extends HBox {

	ProgressBar progressBar;
	Label curTime, maxTime;

	SongProgressView() {
		super();
		progressBar = new ProgressBar();
		progressBar.setMinWidth(300);
		progressBar.setMaxHeight(10);
		curTime = new Label();
		maxTime = new Label();
		setAlignment(Pos.CENTER);
		try {
			applyColorsTo(UIColor.MAIN_COLOR, CSSAttribute.BG_COLOR, progressBar);
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, curTime, maxTime);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		setSpacing(9);
		getChildren().addAll(curTime, progressBar, maxTime);
	}

}
