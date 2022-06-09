package com.dhbw.thesim.core.util;

import com.dhbw.thesim.impexp.JsonHandler;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        exportImagesFromResourcesFolder("/dinosaur");
        exportImagesFromResourcesFolder("/plant");
        exportImagesFromResourcesFolder("/tile");

        loadImages("/dinosaur");
        loadImages("/plant");
        loadImages("/tile");

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
     * Exports an image to the filesystem.
     *
     * @param path The path of the file in the resources. This is also the path of the file in the working directory.
     * @throws IOException If a file was not found or the file count not be created.
     */
    private void exportImage(String path) throws IOException {

        InputStream inputStreamFile = JsonHandler.class.getResourceAsStream(path);
        if (inputStreamFile == null) {
            throw new FileNotFoundException("Cannot find resource file '" + path + "'");
        }

        File file = new File(JsonHandler.getWorkingDirectory() + path);
        if (!file.exists()) {
            Files.copy(inputStreamFile, Path.of(JsonHandler.getWorkingDirectory() + path));
        }

    }

    /**
     * Creats a dictonary on the filesystem inside the working directory.
     *
     * @param folderName The name of the folder, which should be created. (Needs to start with an "/")
     * @see JsonHandler#getWorkingDirectory()
     */
    private void createDirectory(String folderName) {
        File file = new File(JsonHandler.getWorkingDirectory() + folderName);
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Folder created!");
            } else {
                System.err.println("Folder could no be created!");
            }
        }
    }

    /**
     * Loads all images inside the resources folder and exports them to the working directory. {@link JsonHandler#getWorkingDirectory()}.
     *
     * @param folderName The folder name
     * @see JsonHandler
     */
    private void exportImagesFromResourcesFolder(String folderName) {
        URI uri = null;

        try {
            uri = Objects.requireNonNull(SpriteLibrary.class.getResource(folderName)).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        createDirectory(folderName);

        Stream<Path> walk = null;
        try {
            assert uri != null;
            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path folderRootPath = fileSystem.getPath(folderName);
                walk = Files.walk(folderRootPath, 1);
                walk.forEach(childFileOrFolder -> {
                    Image image = new Image(childFileOrFolder.toUri().toString());
                    if (!image.isError()) {
                        try {
                            exportImage(childFileOrFolder.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
     * Loads all images inside a folder in the working directory.
     *
     * @param folderName The name of the folder. (needs to start with an "/")
     * @see JsonHandler#getWorkingDirectory()
     */
    private void loadImages(String folderName) {
        File folder = new File(JsonHandler.getWorkingDirectory() + folderName);
        if (folder.exists() && folder.isDirectory()) {
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.isFile()) {
                    imageMap.put(fileEntry.getName(), new Image(fileEntry.toURI().toString()));
                }
            }
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
