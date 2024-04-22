package com.tkaratas.exifextract;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.*;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadImageController {
    @FXML
    private Button uploadImageButton;
    @FXML
    private Button extractDataButton;
    @FXML
    private Label uploadImageLabel;
    @FXML
    private Label dragAndDropLabel;
    @FXML
    private Pane dragAndDropPane;
    @FXML
    private ImageView imageView;
    private HostServices hostServices;

    private String path;
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void imageViewDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasImage() || dragboard.hasFiles()){
            success = true;
            String imagePath = dragboard.getFiles().get(0).getAbsolutePath();
            uploadImageLabel.setText(imagePath);
            uploadImageLabel.setVisible(true);
            dragAndDropLabel.setVisible(false);
            dragAndDropPane.setBackground(new Background(new BackgroundFill(null, null, null)));
            imageView.setImage((new Image(new File(imagePath).toString())));
            event.consume();
        }


        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void imageViewDragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasImage() || dragboard.hasFiles()){
            event.acceptTransferModes(TransferMode.COPY);
            //dragAndDropLabel.setVisible(false);
            //dragAndDropPane.setBackground(new Background(new BackgroundFill(null, null, null)));
            event.consume();
        }

    }
    @FXML
    private void imageViewDragExited(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if (!(dragboard.hasImage() || dragboard.hasFiles())){
            dragAndDropPane.setBackground(new Background(new BackgroundFill(Color.web("#e9eeff"), null, null)));
            dragAndDropLabel.setVisible(true);
            event.consume();
        }
    }

    @FXML
    private void imageViewDragEntered(DragEvent event) {
        if (event.getGestureSource() != dragAndDropPane && event.getDragboard().hasFiles()) {
            dragAndDropPane.setBackground(new Background(new BackgroundFill(null, null, null)));
            dragAndDropLabel.setVisible(false);
            event.consume();
        }

    }

    @FXML
    protected void onUploadImageButtonClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null){
            uploadImageLabel.setText(selectedFile.getAbsolutePath());
            uploadImageLabel.setVisible(true);
        }else {
            uploadImageLabel.setText("No file selected");
            uploadImageLabel.setVisible(true);
        }
    }

    @FXML
    protected void onExtractDataButtonClick(){
        if (isFilePath(uploadImageLabel.getText())){
            path = uploadImageLabel.getText();
            HashMap<Directory, ArrayList<Tag>> exifData = ExtractExif.extractExifData((path));

            switchToExifScene(exifData);

        }else {
            showErrorPopup("Invalid Path or File", "You choose an invalid Path or File type. Please Choose an image");
        }
    }

    private void switchToExifScene(HashMap<Directory, ArrayList<Tag>> exifData){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("show-details.fxml"));
            Parent exifSceneRoot = loader.load();

            ExifSceneController exifSceneController = loader.getController();
            exifSceneController.setExifData(exifData);
            exifSceneController.setImagePath(path);
            exifSceneController.setHostServices(hostServices);
            exifSceneController.initTreeTableView();

            Stage exifStage = new Stage();
            exifStage.setTitle("EXIF Data");
            exifStage.setScene(new Scene(exifSceneRoot));

            exifStage.initModality(Modality.APPLICATION_MODAL);
            exifStage.initOwner(stage);

            exifStage.showAndWait();


        }catch (IOException e){
            e.printStackTrace();
            showErrorPopup("Error","Failed to load the EXIF scene.");
        }
    }

    public static boolean isFilePath(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    private void showErrorPopup(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setUploadImageLabel(Label uploadImageLabel) {
        this.uploadImageLabel = uploadImageLabel;
    }

    public void setUploadImageButton(Button uploadImageButton) {
        this.uploadImageButton = uploadImageButton;
    }

    public Label getUploadImageLabel() {
        return uploadImageLabel;
    }

    public Button getUploadImageButton() {
        return uploadImageButton;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Button getExtractDataButton() {
        return extractDataButton;
    }

    public Label getDragAndDropLabel() {
        return dragAndDropLabel;
    }

    public Pane getDragAndDropPane() {
        return dragAndDropPane;
    }

    public Stage getStage() {
        return stage;
    }

    public String getPath() {
        return path;
    }
}
