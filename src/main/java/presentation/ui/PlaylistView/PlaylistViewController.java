package presentation.ui.PlaylistView;

import data.Playlist;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.Settings.components.AudioSettingsController;
import presentation.utils.LanguageAssist;

public class PlaylistViewController extends AbstractViewController<FXMain> {


    private TableView<PlaylistView.TrackInfo> table;
    private Label title, info;
    private Playlist playlist;
    private HBox backBox;
    private ObservableList<PlaylistView.TrackInfo> data;

    public PlaylistViewController(FXMain application, Playlist playlist) {
        super(application);

        rootView = new PlaylistView();
        table = ((PlaylistView) rootView).table;
        title = ((PlaylistView) rootView).title;
        info = ((PlaylistView) rootView).info;
        backBox = ((PlaylistView) rootView).backBox;
        this.playlist = playlist;

        initialize();
    }

    @Override
    public void initialize() {
        initPlaylist();

        backBox.setOnMouseClicked(e->{
            application.switchBack();
        });

        AudioSettingsController.importTriggerProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                updatePlaylist();
            }
        });
    }

    private void initPlaylist(){
        LanguageAssist.initLanguage(title, playlist.getTitle());
        updatePlaylist();
    }

    private void updatePlaylist(){
        info.setText(playlist.getSize() + " Titel – " + playlist.getLengthString());
        data = ((PlaylistView) rootView).getTableData(playlist);
        Platform.runLater(()-> table.setItems(data));
    }


}
