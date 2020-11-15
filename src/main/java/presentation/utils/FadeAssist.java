package presentation.utils;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FadeAssist {

    public static void fadeInPane(Pane pane) {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
    }

    public static void fadeOutPane(Pane pane) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
    }

    public static void crossFade(Pane paneToHide, Pane paneToShow) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), paneToHide);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> fadeInPane(paneToShow));
            fadeOut.play();
    }
    
}