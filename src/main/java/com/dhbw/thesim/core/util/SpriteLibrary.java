package com.dhbw.thesim.core.util;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * TODO save images in appdata folder like scenarios
 *
 * @author Daniel Czeschnenr
 */
public class SpriteLibrary {

    private final Map<String, Image> imageMap;

    private static SpriteLibrary INSTANCE;

    private SpriteLibrary() {
        imageMap = new HashMap<>();
        loadImages("dinosaur");
        loadImages("plant");
    }

    public static SpriteLibrary getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpriteLibrary();
        }
        return INSTANCE;
    }

    private void loadImages(String folderName) {
        URI uri = null;
        try {
            uri = Objects.requireNonNull(SpriteLibrary.class.getResource("/" + folderName)).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            assert uri != null;
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path folderRootPath = fileSystem.getPath("/"+folderName);
                Stream<Path> walk = Files.walk(folderRootPath, 1);
                walk.forEach(childFileOrFolder -> {
                    Image image = new Image(childFileOrFolder.toUri().toString());
                    if (!image.isError()){
                        String path = childFileOrFolder.getFileName().toString();
                        //path.substring(0, path.indexOf('.')) //TODO if we only want to store the name of the image. (Maybe better)
                        imageMap.put(path, image);
                    }

                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image getImage(String name){
        Image img = imageMap.get(name);
        if(img == null)
            img = new Image(Objects.requireNonNull(getClass().getResource("undefined.png")).toString());  //TODO
        return img;
    }

}
