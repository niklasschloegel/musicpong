package presentation.ui.Pong;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

public class ScorePane extends BorderPane{
	
	HBox score;
	Label score1;
	Label score2;
	Label doppelPunkt;
	
	public ScorePane() {
		
		score1 = new Label("0");
		score1.setFont(new Font("Futura",30));
		score2 = new Label("0");
		score2.setFont(new Font("Futura",30));
		doppelPunkt = new Label(" : ");
		doppelPunkt.setFont(new Font("Futura",30));
		
		score = new HBox(score1, doppelPunkt, score2);
		score.setPadding(new Insets(50,0,0,0));
		this.setTop(score);
		score.setAlignment(Pos.TOP_CENTER);

		try {
			StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, score1);
			StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, score2);
			StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, doppelPunkt);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
	}
}
