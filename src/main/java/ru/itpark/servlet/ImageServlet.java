package ru.itpark.servlet;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.itpark.service.ImageService;

public class ImageServlet extends HttpServlet {

    private ImageService service;

    @Override
    public void init() {
        try {
            var context = new InitialContext();
            service = (ImageService) context.lookup("java:/comp/env/bean/image-service");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() != null) {
            String[] split = req.getPathInfo().split("/");
            if (split.length != 2) {
                throw new RuntimeException("are you kidding me?");
            }
            service.read(split[1], resp.getOutputStream());

        }
    }
}
