package data;

public class SpotifyNotFoundException extends Exception {

    public SpotifyNotFoundException() {
    }

    public SpotifyNotFoundException(String message) {
        super(message);
    }
}
