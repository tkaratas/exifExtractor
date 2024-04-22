package com.tkaratas.exifextract;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("upload-image.fxml"));
        Parent root = fxmlLoader.load();

        UploadImageController uploadImageController = fxmlLoader.getController();
        uploadImageController.setHostServices(getHostServices());
        uploadImageController.setStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Extract EXIF");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void printWordLengths(List<String>words)
    {
        if (words!=null){
            for (String word:words){
                System.out.println(word.length());
            }
        }
    }
}
