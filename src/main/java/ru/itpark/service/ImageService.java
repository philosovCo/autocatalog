package ru.itpark.service;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.setFollowRedirects;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
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
    private final String DEFAULT_IMAGE = "default_image";

    public ImageService() throws IOException {
        UPLOAD_PATH = System.getenv("UPLOAD_PATH");
        Files.createDirectories(Paths.get(UPLOAD_PATH));
        Path path = Paths.get(UPLOAD_PATH).resolve(DEFAULT_IMAGE);

        if (!Files.exists(path)) {
            File file = new File(path.toString());
            URL url = new URL("https://bezdor4x4.com.ua/uploads/no-image.png");
            FileUtils.copyURLToFile(url, file);

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
        String imageName = DEFAULT_IMAGE;
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
