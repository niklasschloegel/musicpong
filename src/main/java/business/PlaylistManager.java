package business;

import data.Playlist;
import data.Track;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import static data.Konstanten.*;

public class PlaylistManager {


    private static SimpleBooleanProperty isReady = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty easyReady = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty normalReady = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty hardReady = new SimpleBooleanProperty(false);
    private static SimpleIntegerProperty inittedTracks = new SimpleIntegerProperty(0);

    //Titel of Playlists are equally language bundle keys
    public static final Playlist EASY_PLAYLIST = new Playlist("easy");
    public static final Playlist MIDDLE_PLAYLIST = new Playlist("normal");
    public static final Playlist HARD_PLAYLIST = new Playlist("hard");

    private static final Playlist DEFAULT_PLAYLIST = new Playlist();
    private static final Track DEFAULT_TRACK = new Track(Paths.get(MUSIC_PATH + "default.mp3").toAbsolutePath().toString());
    static{
        DEFAULT_PLAYLIST.add(DEFAULT_TRACK);
    }

    static final Playlist TEST = new Playlist("test");

    public static void testPlaylist() {
        File folder = new File(MUSIC_PATH);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile() && !listOfFile.getName().startsWith(".")) {
                Track track = new Track(Paths.get(MUSIC_PATH + listOfFile.getName()).toAbsolutePath().toString());
                TEST.add(track);
            }
        }

        try (PrintWriter out = new PrintWriter("assets/test.csv")) {
            for (Track t : TEST) {
                String spoty = t.isSpotified() ? "spotified" : "alternative";
                out.println(t + ";" + t.getBPM() + ";" + spoty);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    static Playlist getDefaultPlaylist() {
        return DEFAULT_PLAYLIST;
    }

    public static void initPlaylists() {
        Thread a = new Thread(() -> {
            loadPlaylist(EASY_PLAYLIST);
            easyReady.set(true);
        });
        Thread b = new Thread(() -> {
            loadPlaylist(MIDDLE_PLAYLIST);
            normalReady.set(true);
        });
        Thread c = new Thread(() -> {
            loadPlaylist(HARD_PLAYLIST);
            hardReady.set(true);
        });
        a.start(); b.start(); c.start();

        new Thread(() -> {
            while (true) {
                if (!a.isAlive() && !b.isAlive() && !c.isAlive()) {
                    isReady.set(true);
                    break;
                }
            }
        }).start();

    }

    private static void loadPlaylist(Playlist playlist) {
        try {
            Files.lines(Paths.get(PLAYLISTS_PATH + playlist.getTitle() + M3U_DATA_ENDING))
                    .map(String::valueOf).collect(Collectors.toList())
                    .forEach(item -> {
                        playlist.add(new Track(item));
                        inittedTracks.set(inittedTracks.get() + 1);
                    });
        } catch (IOException e) {
            savePlaylist(playlist);
        }

    }

    public static void savePlaylist(Playlist playlist) {
        try (PrintWriter out = new PrintWriter(PLAYLISTS_PATH + playlist.getTitle() + M3U_DATA_ENDING)) {
            playlist.forEach(song -> out.println(song.getSoundFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static long getPlaylistSize(Playlist playlist) throws IOException {
        return Files.lines(Paths.get(PLAYLISTS_PATH + playlist.getTitle() + M3U_DATA_ENDING))
                .count();
    }

    public static long getSongsAmount() {
        try {
            return getPlaylistSize(EASY_PLAYLIST) + getPlaylistSize(MIDDLE_PLAYLIST) + getPlaylistSize(HARD_PLAYLIST);
        } catch (IOException e) {
            return -1L;
        }
    }

    public static boolean getIsReady() {
        return isReady.get();
    }

    public static SimpleBooleanProperty isReadyProperty() {
        return isReady;
    }

    public static boolean isEasyReady() {
        return easyReady.get();
    }

    public static SimpleBooleanProperty easyReadyProperty() {
        return easyReady;
    }

    public static boolean isNormalReady() {
        return normalReady.get();
    }

    public static SimpleBooleanProperty normalReadyProperty() {
        return normalReady;
    }

    public static boolean isHardReady() {
        return hardReady.get();
    }

    public static SimpleBooleanProperty hardReadyProperty() {
        return hardReady;
    }

    public static int getInittedTracks() {
        return inittedTracks.get();
    }

    public static SimpleIntegerProperty inittedTracksProperty() {
        return inittedTracks;
    }
}