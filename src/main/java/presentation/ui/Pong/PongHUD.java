package presentation.ui.Pong;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import presentation.utils.LanguageAssist;
import presentation.utils.StyleAssist;
import presentation.utils.UnsupportedRegionException;

public class PongHUD extends BorderPane{
		
		HBox multi;
		HBox score;
		HBox start;
		GridPane p1Info;
		GridPane p2Info;
		
		Button upArrow1;
		Button downArrow1;
		Button upArrow2;
		Button downArrow2;
		
		Button bTwo;
		Button p1Up;
		Button p1Down;
		Button p2Up;
		Button p2Down;
		Button bSpace;
		Button bEsc;
		
		Label twoText;
		Label spaceText;
		Label escText;
		Label upText1;
		Label downText1;
		Label upText2;
		Label downText2;
		Label press1;
		Label press2;
		
		public PongHUD() {
			
			Insets blyad = new Insets(5,5,5,5);
			
			p1Up = new Button("W");
			p1Up.setMouseTransparent(true);
			p1Up.getStyleClass().add("keys_small");
			p1Down = new Button("S");
			p1Down.setMouseTransparent(true);
			p1Down.getStyleClass().add("keys_small");
			p2Up = new Button("I");
			p2Up.setMouseTransparent(true);
			p2Up.getStyleClass().add("keys_small");
			p2Down = new Button("K");
			p2Down.setMouseTransparent(true);
			p2Down.getStyleClass().add("keys_small");
			
			bSpace = new Button("SPACE");
			bSpace.setMouseTransparent(true);
			bSpace.getStyleClass().add("keys_big");
			bEsc = new Button("ESC");
			bEsc.setMouseTransparent(true);
			bEsc.getStyleClass().add("keys_mid");
			bTwo = new Button("2");
			bTwo.setMouseTransparent(true);
			bTwo.getStyleClass().add("keys_small");
			
			upText1 = new Label();
			downText1 = new Label();
			upText2 = new Label();
			downText2 = new Label();
			twoText = new Label();
			spaceText = new Label();
			escText = new Label();
			press1 = new Label();
			press2 = new Label();
			
			try {
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, upText1);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, downText1);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, upText2);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, downText2);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, twoText);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, spaceText);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, escText);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, press1);
				StyleAssist.applyColorsTo(StyleAssist.UIColor.TEXT_COLOR, StyleAssist.CSSAttribute.TEXT_FILL, press2);
			} catch (UnsupportedRegionException e) {
				e.printStackTrace();
			}
			
			LanguageAssist.initLanguage(upText1, "info.up");
			LanguageAssist.initLanguage(downText1, "info.down");
			LanguageAssist.initLanguage(upText2, "info.up");
			LanguageAssist.initLanguage(downText2, "info.down");
			LanguageAssist.initLanguage(twoText, "info.multiplayer");
			LanguageAssist.initLanguage(spaceText, "info.start");
			LanguageAssist.initLanguage(escText, "info.esc");
			LanguageAssist.initLanguage(press1, "info.press");
			LanguageAssist.initLanguage(press2, "info.press");
			
			upArrow1 = new Button();
			upArrow1.setMouseTransparent(true);
			upArrow1.getStyleClass().add("up");
			downArrow1 = new Button();
			downArrow1.setMouseTransparent(true);
			downArrow1.getStyleClass().add("down");
			upArrow2 = new Button();
			upArrow2.setMouseTransparent(true);
			upArrow2.getStyleClass().add("up");
			downArrow2 = new Button();
			downArrow2.setMouseTransparent(true);
			downArrow2.getStyleClass().add("down");
			
			bTwo.setPadding(blyad);
			p1Up.setPadding(blyad);
			p1Down.setPadding(blyad);
			p2Up.setPadding(blyad);
			p2Down.setPadding(blyad);
			bSpace.setPadding(blyad);
			bEsc.setPadding(blyad);
			twoText.setPadding(blyad);
			spaceText.setPadding(blyad);
			press1.setPadding(blyad);
			press2.setPadding(blyad);
			spaceText.setPadding(blyad);
			escText.setPadding(blyad);
			
			p1Info = new GridPane();
			p1Info.add(upArrow1, 0, 0);
			p1Info.add(p1Up, 1, 0);
			p1Info.add(upText1, 2, 0);
			p1Info.add(downArrow1, 0, 1);
			p1Info.add(p1Down, 1, 1);
			p1Info.add(downText1, 2, 1);
			p1Info.setHgap(10);
			p1Info.setVgap(15);
			p1Info.setPadding(new Insets(50,0,0,50));
			
			p2Info = new GridPane();
			p2Info.add(upText2, 0, 0);
			p2Info.add(p2Up, 1, 0);
			p2Info.add(upArrow2, 2, 0);
			p2Info.add(downText2, 0, 1);
			p2Info.add(p2Down, 1, 1);
			p2Info.add(downArrow2, 2, 1);
			p2Info.setHgap(10);
			p2Info.setVgap(15);
			p2Info.setPadding(new Insets(50,50,0,0));
		
			start = new HBox(press1, bSpace, spaceText);
			start.setPadding(new Insets(0,0,25,0));
			multi = new HBox(press2, bEsc, escText, bTwo, twoText);
			multi.setPadding(new Insets(25,0,0,0));
			
			this.setTop(multi);
			this.setLeft(p1Info);
			this.setRight(p2Info);
			this.setBottom(start);
			p1Info.setAlignment(Pos.CENTER_RIGHT);
			p2Info.setAlignment(Pos.CENTER_LEFT);
			start.setAlignment(Pos.BOTTOM_CENTER);
			multi.setAlignment(Pos.CENTER);
			p2Info.setOpacity(0);
			
		}
}