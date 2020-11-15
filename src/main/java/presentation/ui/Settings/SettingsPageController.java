package presentation.ui.Settings;

import business.MP3Player;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.Settings.components.AudioSettingsController;
import presentation.ui.Settings.components.SettingsController;
import presentation.utils.UnsupportedRegionException;

import static presentation.utils.StyleAssist.*;

public class SettingsPageController extends AbstractViewController<FXMain> {
    //TODO: irgendwas war farbig kacke
    //TODO: menüpunkte für settings
    //TODO: import songs
    //TODO: playlist management, song level inserts
    private AudioSettingsController audioSettingsController;
    private SettingsController settingsController;

    public SettingsPageController(FXMain application, MP3Player mp3Player) {
        super(application, mp3Player);

        rootView = new HBox();
        audioSettingsController = new AudioSettingsController(application, mp3Player);
        settingsController = new SettingsController(application, mp3Player);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.getStyleClass().add("separator");
        try {
            applyColorsTo(UIColor.TEXT_COLOR_DARK, CSSAttribute.BG_COLOR, separator);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        separator.prefHeightProperty().bind(application.getStage().heightProperty());
        audioSettingsController.getRootView().prefWidthProperty()
                .bind(application.getStage().widthProperty().divide(2));
        settingsController.getRootView().prefWidthProperty().bind(application.getStage().widthProperty().divide(2));

        rootView.getChildren().addAll(audioSettingsController.getRootView(), separator,
                settingsController.getRootView());
        try {
            applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, rootView);
        } catch (UnsupportedRegionException e) {
            e.printStackTrace();
        }

        initialize();
    }

    @Override
    public void initialize() {

    }
}
