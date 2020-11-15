package presentation.ui.PlaylistView;

import data.Playlist;
import data.Track;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import presentation.utils.StyleAssist.UIColor;
import presentation.utils.UnsupportedRegionException;

import java.util.ArrayList;
import java.util.List;

import static presentation.utils.LanguageAssist.initLanguage;
import static presentation.utils.StyleAssist.CSSAttribute;
import static presentation.utils.StyleAssist.applyColorsTo;

class PlaylistView extends VBox {

	TableView<TrackInfo> table;
	Label title, info;
	HBox backBox;

	public static class TrackInfo {

		private final Track track;
		private final SimpleStringProperty title, artist, album, length, bpm;

		private TrackInfo(Track track) {
			this.track = track;
			title = new SimpleStringProperty(track.getTitle());
			artist = new SimpleStringProperty(track.getArtist());
			album = new SimpleStringProperty(track.getAlbumTitle());
			length = new SimpleStringProperty(track.getLengthString());
			bpm = new SimpleStringProperty(String.valueOf(Math.round(track.getBPM())));
		}

		public Track getTrack() {
			return track;
		}

		public String getTitle() {
			return title.get();
		}

		public void setTitle(String title) {
			this.title.set(title);
		}

		public String getArtist() {
			return artist.get();
		}

		public void setArtist(String artist) {
			this.artist.set(artist);
		}

		public String getAlbum() {
			return album.get();
		}

		public void setAlbum(String album) {
			this.album.set(album);
		}

		public String getLength() {
			return length.get();
		}

		public void setLength(String length) {
			this.length.set(length);
		}

		public String getBpm() {
			return bpm.get();
		}

		public void setBpm(String bpm) {
			this.bpm.set(bpm);
		}
	}

	PlaylistView() {

		super();

		table = new TableView<>();
		title = new Label();
		info = new Label();

		backBox = new HBox();
		Button back = new Button();
		back.setId("backButton");
		back.setPickOnBounds(true);
		Label backLabel = new Label();
		try{
			applyColorsTo(UIColor.TEXT_COLOR, CSSAttribute.TEXT_FILL, backLabel);
			applyColorsTo(UIColor.SECONDARY_COLOR, CSSAttribute.BG_COLOR, back);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}

		initLanguage(backLabel, "back");
		backBox.getChildren().addAll(back, backLabel);
		backBox.setSpacing(6);
		backBox.setAlignment(Pos.CENTER_LEFT);

		this.getStyleClass().add("playlistView");
		table.getStyleClass().add("playlistTable");
		title.getStyleClass().add("playlistTitle");
		info.getStyleClass().add("playlistInfo");

		TableColumn<TrackInfo, String> song = new TableColumn<>();
		initLanguage(song, "table.song");
		TableColumn<TrackInfo, String> artist = new TableColumn<>();
		initLanguage(artist, "table.artist");
		TableColumn<TrackInfo, String> album = new TableColumn<>();
		initLanguage(album, "table.album");
		TableColumn<TrackInfo, String> songLength = new TableColumn<>();
		initLanguage(songLength, "table.songLength");
		TableColumn<TrackInfo, String> bpm = new TableColumn<>();
		initLanguage(bpm, "table.bpm");

		song.setCellValueFactory(new PropertyValueFactory<>("title"));
		artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
		album.setCellValueFactory(new PropertyValueFactory<>("album"));
		songLength.setCellValueFactory(new PropertyValueFactory<>("length"));
		bpm.setCellValueFactory(new PropertyValueFactory<>("bpm"));

		try {
			applyColorsTo(UIColor.BG_COLOR, CSSAttribute.BG_COLOR, table);
		} catch (UnsupportedRegionException e) {
			e.printStackTrace();
		}
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getColumns().addAll(song, artist, album, songLength, bpm);

		getChildren().addAll(backBox, title, info, table);
		setPadding(new Insets(10));
		setSpacing(4);

	}

	synchronized ObservableList<TrackInfo> getTableData(Playlist playlist){
		List<TrackInfo> data = new ArrayList<>();
		playlist.forEach(track -> data.add(new PlaylistView.TrackInfo(track)));
		return FXCollections.observableList(data);

	}

}
