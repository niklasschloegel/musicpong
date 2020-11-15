package presentation.ui.AudioPlayBar.components;

import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.applyColorsTo;

class VolumeSliderView extends HBox {

	Slider volumeSlider;
	Button volumeIcon;

	VolumeSliderView() {
		super();

		volumeSlider = new JFXSlider();
		volumeSlider.setMin(0.00001);
		volumeSlider.setMax(0.99999);
		volumeSlider.setValue(0.99999);
		volumeSlider.setBlockIncrement(0.01);

		volumeIcon = new Button();
		volumeIcon.setId("volume2");
		volumeIcon.getStyleClass().addAll("whiteButtons", "volume-buttons");
		try {
			applyColorsTo(StyleAssist.UIColor.TEXT_COLOR_DARK, StyleAssist.CSSAttribute.BG_COLOR, volumeIcon);
			applyColorsTo(StyleAssist.UIColor.MAIN_COLOR, StyleAssist.CSSAttribute.BG_COLOR, volumeSlider);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		setSpacing(3);
		setAlignment(Pos.CENTER_RIGHT);
		getChildren().addAll(volumeIcon, volumeSlider);

	}
}
