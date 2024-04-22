package com.tkaratas.exifextract;

import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExifSceneController {
    private HashMap<Directory, ArrayList<Tag>> exifData;
    @FXML
    private TreeTableView<ExifTreeEntry> treeTableView;
    @FXML
    private TreeTableColumn<String, String> directoriesAndTagsCol;
    @FXML
    private TreeTableColumn<String, String> valuesCol;
    @FXML
    private ImageView imageView;
    @FXML
    private Hyperlink googleMapsLink;


    private HostServices hostServices;
    private String lat = "";
    private String longi = "";
    private double decimalLat = Double.NaN;
    private double decimalLong = Double.NaN;



    static boolean isValid(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    public void initTreeTableView(){
        TreeItem<ExifTreeEntry> rootItem = new TreeItem<>(new ExifTreeEntry("Root", "..."));

        directoriesAndTagsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("tag"));
        valuesCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("val"));

        for (Map.Entry<Directory, ArrayList<Tag>> entry : exifData.entrySet()){
            Directory directory = entry.getKey();
            ArrayList<Tag> tags = entry.getValue();

            TreeItem<ExifTreeEntry> directoryName = new TreeItem<>(new ExifTreeEntry(directory.getName(),"..."));

            rootItem.getChildren().add(directoryName);

            for (Tag tag : tags){
                String tagName = tag.getTagName();
                String tagDescription = tag.getDescription();

                if (tagName.equals("GPS Latitude")){
                    lat = tagDescription;
                    decimalLat = convertDMSToDecimal(lat);
                }
                if (tagName.equals("GPS Longitude")){
                    longi = tagDescription;
                    decimalLong = convertDMSToDecimal(longi);
                }

                TreeItem<ExifTreeEntry> tagItem = new TreeItem<>(new ExifTreeEntry(tagName, tagDescription));

                directoryName.getChildren().add(tagItem);
            }
        }

        if (isValid(decimalLat) && isValid(decimalLong)){
            // Create a Google Maps URL with the specified coordinates
            String mapsUrl = "https://www.google.com/maps?q=" + decimalLat + "," + decimalLong;
            googleMapsLink.setText(mapsUrl);
            googleMapsLink.setOnAction(e -> hostServices.showDocument(mapsUrl));
            googleMapsLink.setVisible(true);
        }
        treeTableView.setRoot(rootItem);

    }

    public void setImagePath(String path){
        Image image = new Image(new File(path).toString());
        imageView.setImage(image);
    }
    public void setExifData(HashMap<Directory, ArrayList<Tag>> exifData){
        this.exifData = exifData;
    }

    public double convertDMSToDecimal(String dmsString){
        // Check for a hyphen to determine the sign
        double sign = (dmsString.contains("-")) ? -1.0 : 1.0;
        // Replace commas with periods and remove any non-digit or non-sign characters
        dmsString = dmsString.replace(",", ".");
        dmsString = dmsString.replaceAll("-","");
        // Split the string into degrees, minutes, seconds
        String[] parts = dmsString.split("[°'\"]");

        double degrees = Double.parseDouble(parts[0]);
        double minutes = Double.parseDouble(parts[1]);
        double seconds = Double.parseDouble(parts[2]);


        // Calculate the decimal degrees
        double decimalDegrees = sign * (degrees + (minutes / 60.0) + (seconds / 3600.0));

        return decimalDegrees;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    public HashMap<Directory, ArrayList<Tag>> getExifData() {
        return exifData;
    }

    public TreeTableView<ExifTreeEntry> getTreeTableView() {
        return treeTableView;
    }

    public void setTreeTableView(TreeTableView<ExifTreeEntry> treeTableView) {
        this.treeTableView = treeTableView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
