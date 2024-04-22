package com.tkaratas.exifextract;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ExtractExif {
    public static HashMap<Directory, ArrayList<Tag>> extractExifData(String path) {
        File file = new File(path);
        HashMap<Directory, ArrayList<Tag>> dictionaries = new HashMap<Directory, ArrayList<Tag>>();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            for (Directory directory : metadata.getDirectories()) {
                ArrayList<Tag> tags = new ArrayList<Tag>(directory.getTags());
                dictionaries.put(directory, tags);
            }


            return dictionaries;
        } catch (ImageProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
