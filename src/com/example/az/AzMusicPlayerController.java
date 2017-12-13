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
 * �~���[�W�b�N�v���[���[�̃R���g���[���[�N���X�ł�
 * 
 * @author Azumi Hirata
 *
 */
public class AzMusicPlayerController implements Initializable {
    
    /**
     * �Ȗ����ꗗ�\������ListView
     */
    @FXML
    private ListView<String> listView;
    
    /**
     * �~���[�W�b�N�t�@�C�������s���邽�߂�MediaView
     */
    @FXML
    private MediaView mediaView;

    /**
     * Play�{�^��
     */
    @FXML
    private Button btnPlay;
    
    /**
     * Stop�{�^��
     */
    @FXML
    private Button btnStop;
    
    /**
     * �^�C�g���\�����x��
     */
    @FXML
    private Label lblMusicTitle;
    
    /**
     * Play�{�^�����������ꂽ���̏����ł�
     * 
     * @param event�@�C�x���g
     */
    @FXML
    public void btnPlay_onAction(ActionEvent event) {
        // �I������Ă���^�C�g�����擾����
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null) {
            // �~���[�W�b�N�t�@�C���� ArrayList ����^�C�g�����L�[�Ɍ�������i�K�����݂���j
            for (Path path : getMusicFileList()) {
                if (selectedItem.equals(path.getFileName().toString())) {
                    // �Đ����̃^�C�g����ݒ肷��
                    lblMusicTitle.setText(selectedItem);
                    
                    // �~���[�W�b�N�̍Đ�����������
                    Media media = new Media(path.toUri().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);
                    
                    // �~���[�W�b�N���Đ�����
                    mediaPlayer.play();
                }
            }
        }
    }
    
    /**
     * Stop�{�^�����������ꂽ���̏����ł�
     * 
     * @param event �C�x���g
     */
    @FXML
    public void btnStop_onAction(ActionEvent event) {
        // �~���[�W�b�N�̍Đ����~����
        mediaView.getMediaPlayer().stop();
        
        // �Đ����̃^�C�g��������������
        lblMusicTitle.setText("Please select a title");
    }
    
    /* (�� Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // �~���[�W�b�N�t�@�C���� ArrayList ���擾����
        List<Path> musicFileList = getMusicFileList();
        
        // �~���[�W�b�N�t�@�C�����Ȃ���ΏI��
        if (musicFileList == null) {
            System.out.println("�t�@�C����������܂���");
            Platform.exit();
        }
        
        // �~���[�W�b�N�t�@�C���̃^�C�g����ListView�ɒǉ�����
        for (Path path : musicFileList) {
            listView.getItems().add(path.getFileName().toString());
        }
    }
    
    /**
     * iTunes�̃~���[�W�b�N�t�@�C���� ArrayList ��Ԃ��܂��iWindows�j
     * 
     * @return �~���[�W�b�N�t�@�C���� ArrayList
     */
    private List<Path> getMusicFileList() {
        try {
            // iTunes�̃~���[�W�b�N�t�@�C��(.m4a)�̃��X�g
            List<Path> musicFileList = new ArrayList<>();

            // iTunes Media�t�H���_
            // Windows�̏ꍇ�͂���
            Path start = getMediaDirectory();
            
            // �t�@�C���̒T��
            // �T�u�t�H���_���܂߂ă~���[�W�b�N�t�@�C���������o��
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
     * ���s���� OS ���� iTunes Media �̃��[�g�f�B���N�g������肵�܂�
     * 
     * @return iTunes Media �̃p�X
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
