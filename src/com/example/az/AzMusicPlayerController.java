package com.example.az;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * ミュージックプレーヤーのコントローラークラスです
 * 
 * @author Azumi Hirata
 *
 */
public class AzMusicPlayerController implements Initializable {
    
    /**
     * 曲名を一覧表示するListView
     */
    @FXML
    private ListView<String> listView;
    
    /**
     * ミュージックファイルを実行するためのMediaView
     */
    @FXML
    private MediaView mediaView;

    /**
     * Playボタン
     */
    @FXML
    private Button btnPlay;
    
    /**
     * Stopボタン
     */
    @FXML
    private Button btnStop;
    
    /**
     * タイトル表示ラベル
     */
    @FXML
    private Label lblMusicTitle;
    
    /**
     * Playボタンが押下された時の処理です
     * 
     * @param event　イベント
     */
    @FXML
    public void btnPlay_onAction(ActionEvent event) {
        // 選択されているタイトルを取得する
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null) {
            // ミュージックファイルの ArrayList からタイトルをキーに検索する（必ず存在する）
            for (Path path : getMusicFileList()) {
                if (selectedItem.equals(path.getFileName().toString())) {
                    // 再生中のタイトルを設定する
                    lblMusicTitle.setText(selectedItem);
                    
                    // ミュージックの再生を準備する
                    Media media = new Media(path.toUri().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);
                    
                    // ミュージックを再生する
                    mediaPlayer.play();
                }
            }
        }
    }
    
    /**
     * Stopボタンが押下された時の処理です
     * 
     * @param event イベント
     */
    @FXML
    public void btnStop_onAction(ActionEvent event) {
        // ミュージックの再生を停止する
        mediaView.getMediaPlayer().stop();
        
        // 再生中のタイトルを初期化する
        lblMusicTitle.setText("Please select a title");
    }
    
    /* (非 Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ミュージックファイルの ArrayList を取得する
        List<Path> musicFileList = getMusicFileList();
        
        // ミュージックファイルがなければ終了
        if (musicFileList == null) {
            System.out.println("ファイルが見つかりません");
            Platform.exit();
        }
        
        // ミュージックファイルのタイトルをListViewに追加する
        for (Path path : musicFileList) {
            listView.getItems().add(path.getFileName().toString());
        }
    }
    
    /**
     * iTunesのミュージックファイルの ArrayList を返します（Windows）
     * 
     * @return ミュージックファイルの ArrayList
     */
    private List<Path> getMusicFileList() {
        try {
            // iTunesのミュージックファイル(.m4a)のリスト
            List<Path> musicFileList = new ArrayList<>();

            // iTunes Mediaフォルダ
            // Windowsの場合はここ
            Path start = getMediaDirectory();
            
            // ファイルの探索
            // サブフォルダを含めてミュージックファイルだけ取り出す
            FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".m4a")) {
                        musicFileList.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

            };

            Files.walkFileTree(start, visitor);
            
            return musicFileList;
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * 実行中の OS から iTunes Media のルートディレクトリを特定します
     * 
     * @return iTunes Media のパス
     */
    private Path getMediaDirectory() {
        String osName = System.getProperty("os.name");
        if (osName.indexOf("Windows") >= 0) {
            // Running on Windows
            return Paths.get(System.getProperty("user.home"), "Music", "iTunes", "iTunes Media");
        } else if (osName .indexOf("Mac") >= 0) {
            // Running on macOS
            throw new UnsupportedOperationException();
        } else if (osName.indexOf("Linux") >= 0) {
            // Running on Linux
            throw new UnsupportedOperationException();
        } else {
            // Running on other systems
            throw new UnsupportedOperationException();
        }
    }
}
