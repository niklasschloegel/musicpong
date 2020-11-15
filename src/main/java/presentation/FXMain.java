package presentation;

import business.MP3Player;
import business.PlaylistManager;
import business.PongLogic;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import presentation.ui.PlaylistView.PlaylistViewController;
import presentation.ui.Pong.PongViewController;
import presentation.ui.Scenes;
import presentation.ui.Settings.SettingsPageController;
import presentation.ui.StartScreen.StartScreenController;
import presentation.utils.LanguageAssist;
import presentation.utils.StartAssist;
import presentation.utils.StyleAssist;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Stack;

public class FXMain extends Application {
    private Pane caller, currentPane;
    private static Scene scene;
    private SimpleDoubleProperty width;
    private SimpleDoubleProperty heigth;
    private HashMap<Scenes, Pane> scenes;
    private static Stage myStage;
    private Canvas canvas;
    private PongLogic pong;
    private Scenes tmpCaller, tmpCurrent;
    private MP3Player mp3;
    private Stack<Pane> callerStack;

    @Override
    public void init() {
        width = new SimpleDoubleProperty(1240);
        heigth = new SimpleDoubleProperty(720);
        mp3 = new MP3Player();
        callerStack = new Stack<>();
        scenes = new HashMap<>();

        //PlaylistManager.testPlaylist();

        mp3.trackPropProperty().addListener((observableValue, track, t) -> {
            Image img = null;
            if (t.getImage() == null || t.getImage().length <= 0) {
                try {
                    img = new Image(new FileInputStream(System.getProperty("user.dir") + "/assets/images/defaultCover.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                img = new Image(new ByteArrayInputStream(t.getImage()));
            }
            StyleAssist.refreshColors(img);
        });
    }

    @Override
    public void start(Stage stage) {
        myStage = stage;
        myStage.setMinHeight(650);
        myStage.setMinWidth(974);
        try {
            try {
                StartAssist.loadConfigs();
            } catch (Exception e) {
                PongViewController.setFps(30);
                PongViewController.setHudShow(true);
                LanguageAssist.loadLanguage(LanguageAssist.SupportedLanguage.ENGLISH);
            }

            mp3.setPlaylist(mp3.getDefaultPlaylist());
            mp3.play(mp3.getCurrentTrack().getSoundFile());

            scenes.put(Scenes.HOME_VIEW, new StartScreenController(this, mp3).getRootView());
            scenes.put(Scenes.PONG_VIEW, new PongViewController(this, mp3).getRootView());

            scene = new Scene(scenes.get(Scenes.HOME_VIEW));

            try {
                scene.getStylesheets().add(Paths.get(System.getProperty("user.dir") + "/assets/css/application.css").toUri().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            stage.setScene(scene);
            stage.setTitle("Pong with Music - by Matteo, Niklas and Sandra");
            stage.getIcons().add(new Image("file:assets/images/mini.png"));
            stage.show();
            stage.setOnHidden(e -> {
                StartAssist.saveConfigs();
                System.exit(0);
            });

            initRestViews();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return myStage;
    }

    public Scene getScene() {
        return scene;
    }

    public void switchScene(Scenes newView, Scenes caller) {
        Pane nextScene;

//        tmpCaller = caller;
//        tmpCurrent = newView;
//
//        double tmpWidth = scenes.get(caller).getWidth();
//
//        KeyFrame start = new KeyFrame(Duration.ZERO,
//        		new KeyValue(scenes.get(newView).translateXProperty(), tmpWidth),
//        		new KeyValue(scenes.get(caller).translateXProperty(), 0));
//        KeyFrame end = new KeyFrame(Duration.millis(400),
//        		new KeyValue(scenes.get(newView).translateXProperty(), 0),
//        		new KeyValue(scenes.get(caller).translateXProperty(), - tmpWidth));
//        Timeline slide = new Timeline(start, end);
//

        if (scenes.containsKey(newView)) {
        	nextScene = scenes.get(newView);
        	scene.setRoot(nextScene);
//        	slide.setOnFinished(e -> scene.setRoot(nextScene));
//        	slide.play();

			currentPane = nextScene;

        }

        if (scenes.containsKey(caller)) {
            this.callerStack.push(scenes.get(caller));
//            System.out.println("STACK:");
//            callerStack.stream().map(Node::toString).forEach(System.out::println);
        }
    }

    public void switchBack() {
//    	double tmpWidth = scenes.get(tmpCurrent).getWidth();
//
//        KeyFrame start = new KeyFrame(Duration.ZERO,
//        		new KeyValue(callerStack.peek().translateXProperty(), -tmpWidth),
//        		new KeyValue(scenes.get(tmpCurrent).translateXProperty(), 0));
//        KeyFrame end = new KeyFrame(Duration.millis(400),
//        		new KeyValue(callerStack.peek().translateXProperty(), 0),
//        		new KeyValue(scenes.get(tmpCurrent).translateXProperty(), tmpWidth));
//        Timeline slide = new Timeline(start, end);
//        slide.setOnFinished(e -> scene.setRoot(callerStack.pop()));
//        slide.play();

    	scene.setRoot(callerStack.pop());
//        System.out.println("STACK:");
//        callerStack.stream().map(Node::toString).forEach(System.out::println);
    }

    private void initRestViews() {
        scenes.put(Scenes.SETTINGS_VIEW, new SettingsPageController(this, mp3).getRootView());
        scenes.put(Scenes.EASY_PLAYLIST, new PlaylistViewController(this, PlaylistManager.EASY_PLAYLIST).getRootView());
        scenes.put(Scenes.MIDDLE_PLAYLIST, new PlaylistViewController(this, PlaylistManager.MIDDLE_PLAYLIST).getRootView());
        scenes.put(Scenes.HARD_PLAYLIST, new PlaylistViewController(this, PlaylistManager.HARD_PLAYLIST).getRootView());
        PlaylistManager.initPlaylists();
    }

    public static Scene getStaticScene(){
        return scene;
    }

    public static Stage getStaticStage() { return myStage;}
}