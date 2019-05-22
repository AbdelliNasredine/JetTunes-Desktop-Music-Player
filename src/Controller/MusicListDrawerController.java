package Controller;

import Model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicListDrawerController implements Initializable {

    @FXML
    private ListView<Song> musicList;
    public MusicPlayerController controller;
    ObservableList<Song> musicListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = MusicPlayerController.instance;
        settingUpMusicList();
        musicList.setItems(musicListObservableList);
        musicList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                controller.playSong(musicList.getSelectionModel().getSelectedIndex()));
        musicList.setCellFactory(param -> new SongCellController());
    }

    void settingUpMusicList() {
        for (File file : MusicPlayerController.musicList) {
            Media hit = new Media(file.toURI().toString());
            String songName, artistName, songLength;
            Image albumImage;
            //assigning song name
            if (hit.getMetadata().get("songArtist") == null) {
                songName = hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " ");
            } else songName = "" + hit.getMetadata().get("songArtist");
            //assigning song album name
            if (hit.getMetadata().get("songArtist") == null) {
                artistName = hit.getSource().split("/")[hit.getSource().split("/").length - 1].replace("%20", " ");
            } else artistName = "" + hit.getMetadata().get("songArtist");
            //assigning song length
            int seconds = (int) hit.getDuration().toSeconds() % 60;
            int minutes = (int) hit.getDuration().toSeconds() / 60;
            songLength = String.format("%02d:%02d", minutes, seconds);
            //assigning song album image
            albumImage = (Image) hit.getMetadata().get("image");
            System.out.println(albumImage);
            musicListObservableList.add(new Song(songName, artistName, songLength, albumImage));
        }
    }

    @FXML
    void closeDrawer(MouseEvent event) {
        controller.deactivateDrawer(null);
    }

}
