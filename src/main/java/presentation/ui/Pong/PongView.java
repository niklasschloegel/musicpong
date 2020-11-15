package presentation.ui.Pong;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.*;

public class PongView extends Pane {
	GraphicsContext gc;
	Canvas curCanvas;
	Rectangle rec1;
	Rectangle rec2;
	Rectangle rec1Fade;
	Rectangle rec1Scale;
	Ellipse oval;
	Ellipse ovalShadow;
	Ellipse ovalInner;
	FadeTransition ft;
	ScaleTransition st;
	ScaleTransition stRec1;
	FadeTransition ftRec1;
	ScaleTransition stRec2;
	FadeTransition ftShadow;
	ScaleTransition stShadow;
	FadeTransition ftInner;
	ScaleTransition stInner;
	ParallelTransition pT;
	ParallelTransition pT2;

	/**
	 * instatiates the shape objects needed to realize the game
	 * 
	 * @param canvas the "stage" to put the objects onto
	 */
	public PongView(Canvas canvas) {
		super();

		curCanvas = canvas;

		try {
			applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, this);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		rec1Scale = new Rectangle();
		rec1Fade = new Rectangle();

		oval = new Ellipse();
		ovalShadow = new Ellipse();
		ovalInner = new Ellipse();

		rec1Scale.xProperty().bind(rec1.xProperty());
		rec1Scale.yProperty().bind(rec1.yProperty());
		rec1Scale.widthProperty().bind(rec1.widthProperty());

		rec1Fade.xProperty().bind(rec1.xProperty());
		rec1Fade.yProperty().bind(rec1.yProperty());
		rec1Fade.widthProperty().bind(rec1.widthProperty());

		ovalShadow.centerXProperty().bind(oval.centerXProperty());
		ovalShadow.centerYProperty().bind(oval.centerYProperty());
		ovalShadow.radiusXProperty().bind(oval.radiusXProperty());
		ovalShadow.radiusYProperty().bind(oval.radiusYProperty());

		ovalInner.centerXProperty().bind(oval.centerXProperty());
		ovalInner.centerYProperty().bind(oval.centerYProperty());
		ovalInner.radiusXProperty().bind(oval.radiusXProperty());
		ovalInner.radiusYProperty().bind(oval.radiusYProperty());
		
		ft = new FadeTransition(Duration.millis(800), oval);
		ft.setFromValue(1);
		ft.setToValue(0.4);
		ft.setCycleCount(Timeline.INDEFINITE);
		ft.setAutoReverse(false);

		stInner = new ScaleTransition(Duration.millis(800), ovalInner);
		stInner.setToX(1f);
		stInner.setToY(1f);
		stInner.setFromX(oval.getRadiusX());
		stInner.setFromY(oval.getRadiusY());
		stInner.setCycleCount(Timeline.INDEFINITE);
		stInner.setAutoReverse(false);

		ftShadow = new FadeTransition(Duration.millis(1000), ovalShadow);
		ftShadow.setFromValue(1);
		ftShadow.setToValue(0.4);
		ftShadow.setCycleCount(Timeline.INDEFINITE);
		ftShadow.setAutoReverse(false);

		stShadow = new ScaleTransition(Duration.millis(800), ovalShadow);
		stShadow.setToX(1.2f);
		stShadow.setToY(1.2f);
		stShadow.setFromX(oval.getRadiusX());
		stShadow.setFromY(oval.getRadiusY());
		stShadow.setCycleCount(Timeline.INDEFINITE);
		stShadow.setAutoReverse(false);

//		ftRec1 = new FadeTransition(Duration.millis(800), rec1Fade);
//		stRec1 = new ScaleTransition(Duration.millis(800), rec1Scale);

		pT = new ParallelTransition(oval, ft, stShadow, stInner);
		
//		pT = new ParallelTransition(oval, ft, stShadow, stInner, stRec1);

		this.getChildren().addAll(curCanvas, rec1, rec2, oval, ovalInner);

	}
}