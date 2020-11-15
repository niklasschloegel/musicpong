package presentation.ui.Settings.components;

import business.MP3Player;
import business.PlaylistManager;
import data.Track;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import presentation.FXMain;
import presentation.ui.AbstractViewController;
import presentation.ui.Scenes;
import presentation.utils.LanguageAssist;
import presentation.utils.PopUpAssist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static presentation.utils.FadeAssist.*;

public class AudioSettingsController extends AbstractViewController<FXMain> {

    private boolean easyUpdate, middleUpdate, hardUpdate;
    private Button easy, middle, hard, importButton;
    private HBox importBox, successBox, backBox;
    private VBox loadingBox;
    private ProgressBar progress;
    private long amountSongs;
    private SimpleIntegerPropertyWithListener<AddListener> added;
    private static SimpleBooleanProperty importTrigger = new SimpleBooleanProperty(false);

    private class AddListener implements ChangeListener<Number> {

        private List<File> files;

        private AddListener(List<File> files) {
            this.files = files;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            Platform.runLater(() -> progress.setProgress(t1.doubleValue() / files.size()));
           // System.out.println(progress.getProgress() + " progress," + t1.doubleValue() + " t1");
            if (t1.doubleValue() == files.size()) {
                fadeOutPane(loadingBox);
                added.set(0);
            }
        }

        public void setFiles(List<File> files) {
            this.files = files;
        }
    }

    private static class SimpleIntegerPropertyWithListener<T extends ChangeListener<Number>> extends SimpleIntegerProperty {

        private T listener;

        private SimpleIntegerPropertyWithListener(int val) {
            super(val);
        }

        public void setListener(T listener) {
            this.listener = listener;
            addListener(listener);
        }

        public T getListener() {
            return listener;
        }
    }

    public AudioSettingsController(FXMain application, MP3Player mp3Player) {
        super(application, mp3Player);
        rootView = new AudioSettingsView();

        easy = ((AudioSettingsView) rootView).easy;
        middle = ((AudioSettingsView) rootView).middle;
        hard = ((AudioSettingsView) rootView).hard;
        importBox = ((AudioSettingsView) rootView).importBox;
        importButton = ((AudioSettingsView) rootView).importButton;
        successBox = ((AudioSettingsView) rootView).successBox;
        loadingBox = ((AudioSettingsView) rootView).loadingBox;
        backBox = ((AudioSettingsView) rootView).backBox;
        progress = ((AudioSettingsView) rootView).progress;
        progress.setProgress(0);
        amountSongs = PlaylistManager.getSongsAmount();
        added = new SimpleIntegerPropertyWithListener<>(0);

        easy.prefWidthProperty().bind(importBox.widthProperty().multiply(0.9));
        middle.prefWidthProperty().bind(importBox.widthProperty().multiply(0.9));
        hard.prefWidthProperty().bind(importBox.widthProperty().multiply(0.9));

        initialize();
    }

    @Override
    public void initialize() {
        importBox.setOnDragOver(event -> {
            boolean dropSupported = true;
            Dragboard dragboard = event.getDragboard();

            if (!dragboard.hasFiles()) {
                dropSupported = false;
            } else {
                List<File> files = dragboard.getFiles();
                for (File f : files) {
                    String s = f.toString();
                    if (!s.endsWith(".mp3")) {
                        dropSupported = false;
                    }
                }
            }

            if (dropSupported) {
                event.acceptTransferModes(TransferMode.ANY);
            }

        });

        importBox.setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();
            List<File> files = dragboard.getFiles();
            importSongs(files);
        });

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3-Files", "*.mp3"));
            List<File> files = fileChooser.showOpenMultipleDialog(application.getStage().getOwner());
            if (files != null && files.size() > 0) {
                importSongs(files);
            }
        });

        backBox.setOnMouseClicked(e -> application.switchBack());

        if (amountSongs <= 0) {
            loadingBox.setOpacity(0);
        }

        PlaylistManager.inittedTracksProperty().addListener((observableValue, number, t1) -> {
            Platform.runLater(() -> progress.setProgress(t1.doubleValue() / amountSongs));
            importTrigger.set(true);
            importTrigger.set(false);
            if (t1.doubleValue() == amountSongs) {
                crossFade(loadingBox, successBox);
                new Thread(() -> {
                    try {
                        sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    fadeOutPane(successBox);
                }).start();
            }
        });

        importTriggerProperty().addListener((observableValue, aBoolean, t1) -> {

            if (PlaylistManager.EASY_PLAYLIST.getSize() > 0) {
                Platform.runLater(() -> {
                    ImageView cover = new ImageView(new Image(new ByteArrayInputStream(PlaylistManager.EASY_PLAYLIST.getTrack(0).getImage())));
                    cover.setFitWidth(70);
                    cover.setFitHeight(70);
                    easy.setGraphic(cover);
                });
            }
            if (PlaylistManager.MIDDLE_PLAYLIST.getSize() > 0) {
                Platform.runLater(() -> {
                    ImageView cover = new ImageView(new Image(new ByteArrayInputStream(PlaylistManager.MIDDLE_PLAYLIST.getTrack(0).getImage())));
                    cover.setFitWidth(70);
                    cover.setFitHeight(70);
                    middle.setGraphic(cover);
                });
            }

            if (PlaylistManager.HARD_PLAYLIST.getSize() > 0) {
                Platform.runLater(() -> {
                    ImageView cover = new ImageView(new Image(new ByteArrayInputStream(PlaylistManager.HARD_PLAYLIST.getTrack(0).getImage())));
                    cover.setFitWidth(70);
                    cover.setFitHeight(70);
                    hard.setGraphic(cover);
                });
            }

        });

        easy.setOnAction(e -> application.switchScene(Scenes.EASY_PLAYLIST, Scenes.SETTINGS_VIEW));

        middle.setOnAction(e -> application.switchScene(Scenes.MIDDLE_PLAYLIST, Scenes.SETTINGS_VIEW));

        hard.setOnAction(e -> application.switchScene(Scenes.HARD_PLAYLIST, Scenes.SETTINGS_VIEW));
    }

    private void importSongs(List<File> files) {

        fadeInPane(loadingBox);
        progress.setProgress(0);

        if (added.getListener() == null) {
            added.setListener(new AddListener(files));
        } else {
            added.getListener().setFiles(files);
        }

        new Thread(() -> {
            StringBuilder info = new StringBuilder();
            StringBuilder easy = new StringBuilder(":\n");
            StringBuilder normal = new StringBuilder(":\n");
            StringBuilder hard = new StringBuilder(":\n");

            info.append(LanguageAssist.getValue("import.info")).append("\n");
            easyUpdate = middleUpdate = hardUpdate = false;

            files.stream().map(file -> file.toPath().toAbsolutePath().toString())
                    .filter(Objects::nonNull).filter(string -> string.endsWith(".mp3")).map(Track::new)
                    .forEach(t -> {
                        float bpm = t.getBPM();
                        String add = String.format("%s - %s: %3.2f bpm\n", t.getArtist(), t.getTitle(), bpm);
                        if (bpm <= 107) {
                            easy.append(add);
                            PlaylistManager.EASY_PLAYLIST.add(t);
                            easyUpdate = true;
                        } else if (bpm >= 107 && bpm <= 134) {
                            normal.append(add);
                            PlaylistManager.MIDDLE_PLAYLIST.add(t);
                            middleUpdate = true;
                        } else {
                            hard.append(add);
                            PlaylistManager.HARD_PLAYLIST.add(t);
                            hardUpdate = true;
                        }
                        added.set(added.get() + 1);
                    });
            if (easyUpdate) {
                info.append("\nPlaylist ").append(LanguageAssist.getValue("easy")).append(easy);
                PlaylistManager.savePlaylist(PlaylistManager.EASY_PLAYLIST);
            }
            if (middleUpdate) {
                info.append("\nPlaylist ").append(LanguageAssist.getValue("normal")).append(normal);
                PlaylistManager.savePlaylist(PlaylistManager.MIDDLE_PLAYLIST);
            }
            if (hardUpdate) {
                info.append("\nPlaylist ").append(LanguageAssist.getValue("hard")).append(hard);
                PlaylistManager.savePlaylist(PlaylistManager.HARD_PLAYLIST);
            }

            if (easyUpdate || middleUpdate || hardUpdate) {
                Platform.runLater(() -> {
                    Stage importInfo = PopUpAssist.createPopUp(new ImportInfoView(info.toString()), application.getStage());
                    PopUpAssist.center(importInfo, application.getStage());
                    importInfo.setTitle(LanguageAssist.getValue("import.header"));
                    importInfo.show();
                    importTrigger.set(true);
                    importTrigger.set(false);
                });
            }

        }).start();
    }

    public static SimpleBooleanProperty importTriggerProperty() {
        return importTrigger;
    }
}
