package ru.itpark.service;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.setFollowRedirects;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;

public class ImageService {

    private final String UPLOAD_PATH;
    private final String DEFAULT_IMAGE_NAME = "default_image";
    private final URI DEFAULT_IMAGE_FILE_PATH;

    public ImageService() throws IOException, URISyntaxException {
        UPLOAD_PATH = System.getenv("UPLOAD_PATH");
        DEFAULT_IMAGE_FILE_PATH = getClass().getClassLoader()
                .getResource(DEFAULT_IMAGE_NAME + ".jpg").toURI();
        Files.createDirectories(Paths.get(UPLOAD_PATH));
        Path path = Paths.get(UPLOAD_PATH).resolve(DEFAULT_IMAGE_NAME);

        if (!Files.exists(path)) {
            FileUtils.copyFile(
                    Paths.get(DEFAULT_IMAGE_FILE_PATH).toFile(),
                    Files.createFile(path).toFile());
        }
    }

    public void read(String id, ServletOutputStream outputStream) throws IOException {
        Path path = Paths.get(UPLOAD_PATH).resolve(id);
        if (!Files.exists(path)) {
            throw new RuntimeException("404");
        }
        Files.copy(path, outputStream);
    }

    public String write(Part part) throws IOException {
        String id = UUID.randomUUID().toString();
        part.write(Paths.get(UPLOAD_PATH).resolve(id).toString());
        return id;
    }

    public String getImageByUrl(String picture_url) {
        String imageName = DEFAULT_IMAGE_NAME;
        try {
            URL url = new URL(picture_url);

            setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");

            String name = getBaseName(url.getPath());
            String upload_path = System.getenv("UPLOAD_PATH");
            Path path = Paths.get(upload_path).resolve(name);

            if (con.getResponseCode() == HTTP_OK && Files.exists(path)) {
                imageName = name;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageName;
    }
}
