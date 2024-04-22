module com.tkaratas.exifextract {
    requires javafx.controls;
    requires javafx.fxml;
    requires metadata.extractor;


    opens com.tkaratas.exifextract to javafx.fxml;
    exports com.tkaratas.exifextract;
}