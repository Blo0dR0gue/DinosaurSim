package com.dhbw.thesim.core.util;

import javafx.scene.image.Image;

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
 * The sprite library for the simulation. <br>
 * Uses the Singleton-Pattern.
 * TODO save images in appdata folder like scenarios
 *
 * @author Daniel Czeschnenr
 */
public class SpriteLibrary {

    /**
     * The map with all images.
     */
    private final Map<String, Image> imageMap;

    /**
     * The instance of the library object.
     */
    private static SpriteLibrary INSTANCE;

    /**
     * Constructor
     */
    private SpriteLibrary() {
        imageMap = new HashMap<>();
        loadImages("dinosaur");
        loadImages("plant");
        loadImages("tile");
    }

    /**
     * Gets and/or creates a instance of the {@link SpriteLibrary}
     *
     * @return The {@link SpriteLibrary} {@link #INSTANCE} object.
     */
    public static SpriteLibrary getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpriteLibrary();
        }
        return INSTANCE;
    }

    /**
     * Loads all images inside a folder.
     *
     * @param folderName The folder name
     */
    private void loadImages(String folderName) {
        URI uri = null;
        try {
            uri = Objects.requireNonNull(SpriteLibrary.class.getResource("/" + folderName)).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Stream<Path> walk = null;
        try {
            assert uri != null;
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path folderRootPath = fileSystem.getPath("/" + folderName);
                walk = Files.walk(folderRootPath, 1);
                walk.forEach(childFileOrFolder -> {
                    Image image = new Image(childFileOrFolder.toUri().toString());
                    if (!image.isError()) {
                        String path = childFileOrFolder.getFileName().toString();
                        //path.substring(0, path.indexOf('.')) //TODO if we only want to store the name of the image. (Maybe better)
                        imageMap.put(path, image);
                    }

                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (walk != null)
                walk.close();
        }

    }

    /**
     * Gets the defined image or the undefined image
     *
     * @param name The name of the image.
     * @return A {@link Image} object
     */
    public Image getImage(String name) {
        Image img = imageMap.get(name);
        if (img == null)
            img = new Image(Objects.requireNonNull(getClass().getResource("/helper/undefined.png")).toString());
        return img;
    }

}
