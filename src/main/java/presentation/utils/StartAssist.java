package presentation.utils;

import javafx.stage.Stage;
import presentation.FXMain;
import presentation.ui.Pong.PongViewController;

import java.io.*;
import java.util.Properties;

public class StartAssist {

    private static final String CONFIG_PATH = "assets/config/config.properties";
    private static final String SCREEN_SIZE_X = "screen_size_x";
    private static final String SCREEN_SIZE_Y = "screen_size_y";
    private static final String SCREEN_POS_X = "screen_pos_x";
    private static final String SCREEN_POS_Y = "screen_pos_y";
    private static final String FPS = "fps";
    private static final String HUD = "hud";
    private static final String LANGUAGE = "language";
    public static final String SPOTIFY_CLIENT_ID = "spotify_client_id";
    public static final String SPOTIFY_CLIENT_SECRET = "spotify_client_secret";


    public static void saveConfigs() {

        try (OutputStream out = new FileOutputStream(CONFIG_PATH)) {
            Properties properties = new Properties();
            Stage stage = FXMain.getStaticStage();

            String screenSizeX = String.valueOf(stage.getWidth());
            String screenSizeY = String.valueOf(stage.getHeight());
            String screenPosX = String.valueOf(stage.getX());
            String screenPosY = String.valueOf(stage.getY());
            String fps = String.valueOf(PongViewController.getFps());
            String hud = PongViewController.isHudShow() ? "true" : "false";
            String language = LanguageAssist.getCurrentLanguage().toString();

            properties.setProperty(SCREEN_SIZE_X, screenSizeX);
            properties.setProperty(SCREEN_SIZE_Y, screenSizeY);
            properties.setProperty(SCREEN_POS_X, screenPosX);
            properties.setProperty(SCREEN_POS_Y, screenPosY);
            properties.setProperty(FPS, fps);
            properties.setProperty(HUD, hud);
            properties.setProperty(LANGUAGE, language);
            properties.setProperty(SPOTIFY_CLIENT_ID, System.getProperty(SPOTIFY_CLIENT_ID));
            properties.setProperty(SPOTIFY_CLIENT_SECRET, System.getProperty(SPOTIFY_CLIENT_SECRET));

            properties.store(out, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadConfigs() throws Exception {
        InputStream input = new FileInputStream(CONFIG_PATH);
        Properties properties = new Properties();
        properties.load(input);

        try {
            System.setProperty(SPOTIFY_CLIENT_ID, properties.getProperty(SPOTIFY_CLIENT_ID));
            System.setProperty(SPOTIFY_CLIENT_SECRET, properties.getProperty(SPOTIFY_CLIENT_SECRET));
            double screenSizeX = Double.parseDouble(properties.getProperty(SCREEN_SIZE_X));
            double screenSizeY = Double.parseDouble(properties.getProperty(SCREEN_SIZE_Y));
            double screenPosX = Double.parseDouble(properties.getProperty(SCREEN_POS_X));
            double screenPosY = Double.parseDouble(properties.getProperty(SCREEN_POS_Y));
            int fps = Integer.parseInt(properties.getProperty(FPS));
            boolean hud = properties.getProperty(HUD).equals("true");

            String language = properties.getProperty(LANGUAGE);
            LanguageAssist.SupportedLanguage supportedLanguage;
            switch (language) {
                case "Deutsch":
                    supportedLanguage = LanguageAssist.SupportedLanguage.GERMAN;
                    break;
                default:
                    supportedLanguage = LanguageAssist.SupportedLanguage.ENGLISH;
                    break;
            }

            FXMain.getStaticStage().setMinWidth(screenSizeX);
            FXMain.getStaticStage().setMinHeight(screenSizeY);
            FXMain.getStaticStage().setX(screenPosX);
            FXMain.getStaticStage().setY(screenPosY);
            PongViewController.setFps(fps);
            PongViewController.setHudShow(hud);
            LanguageAssist.loadLanguage(supportedLanguage);
        } finally {
            input.close();
        }

    }


}