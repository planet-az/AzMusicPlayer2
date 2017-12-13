package com.example.az;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * ミュージックプレーヤーの起動クラスです
 * 
 * @author Azumi Hitata
 *
 */
public class AzMusicPlayerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("AzMusicPlayerView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Az Music Player Ver.2");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
