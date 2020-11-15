package data;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.AudioAnalysis;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioAnalysisForTrackRequest;
import presentation.utils.StartAssist;
import v4lk.lwbd.BeatDetector;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class Beat {

    private static final double MIN_ENERGY = 0.105;

    private static final String clientID = System.getProperty(StartAssist.SPOTIFY_CLIENT_ID);
    private static final String clientSecret = System.getProperty(StartAssist.SPOTIFY_CLIENT_SECRET);

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientID)
            .setClientSecret(clientSecret)
            .build();

    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

    static float getSpotifysBPM(data.Track track) throws SpotifyNotFoundException {
        String title = track.getTitle();
        String artist = track.getArtist() != null ? track.getArtist() : "";
        String search = String.format("%s %s", title, artist);
//        System.out.println("\nSEARCH:\n" + search);

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(search).build();

            final Paging<Track> trackPaging = searchTracksRequest.execute();

            Track[] t = trackPaging.getItems();
            if (t.length > 0) {
                GetAudioAnalysisForTrackRequest getAudioAnalysisForTrackRequest = spotifyApi.getAudioAnalysisForTrack(t[0].getId()).build();
                AudioAnalysis audioAnalysis = getAudioAnalysisForTrackRequest.execute();
//                System.out.println("RESULT: " +
//                        Arrays.stream(t[0].getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", "))
//                        + " " + t[0].getName() + "\n" + audioAnalysis.getTrack().getTempo() + " bpm");
                return audioAnalysis.getTrack().getTempo();
            } else {
                throw new SpotifyNotFoundException("Spotify did not get any results for that request");
            }

        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            throw new SpotifyNotFoundException();
        }

    }

    static float getAlternativeBPM(data.Track track) {
        v4lk.lwbd.util.Beat[] beats;
        try {
            beats = BeatDetector.detectBeats(new File(track.getSoundFile()), BeatDetector.AudioType.MP3);
            long amountBeats = Arrays.stream(beats).filter(beat -> beat.energy > MIN_ENERGY).count();
            float erg = (float) amountBeats / track.getLength() * 60;
           // System.out.println("Alternative: " + erg + " bpm");
            return erg;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
//        System.out.println(track.getTitle());
//
//        System.out.println(" – beat Array:\n" + Arrays.toString(beats));
//
//        Map<Double, Integer> counter = new HashMap<>();
//        Arrays.stream(beats).map(beat -> Math.round(beat.energy * 100.0) / 100.0).forEach(energy -> {
//
//            if (!counter.containsKey(energy)) {
//                counter.put(energy, 1);
//            } else {
//                counter.put(energy, counter.get(energy) + 1);
//            }
//
//        });
//        List<Map.Entry<Double, Integer>> s = new ArrayList<>(counter.entrySet());
//        s.sort(Comparator.comparingInt(Map.Entry::getValue));
//        System.out.println(s.toString());
//
//        s.sort(Comparator.comparingDouble(Map.Entry::getKey));
//        System.out.println(s.toString());

    }


}
