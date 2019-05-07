package ru.itpark.servlet;

import static ru.itpark.utils.DBUtils.toMapWithSingleValue;

import java.io.IOException;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.itpark.domain.Auto;
import ru.itpark.service.AutoService;
import ru.itpark.service.ImageService;

public class AutoSaveServlet extends HttpServlet {

    private AutoService autoService;
    private ImageService imageService;

    @Override
    public void init() {
        try {
            var context = new InitialContext();
            autoService = (AutoService) context.lookup("java:/comp/env/bean/auto-service");
            imageService = (ImageService) context.lookup("java:/comp/env/bean/image-service");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                var autos = autoService.getAutoByParams(Map.of("id", split[1]));
                req.setAttribute("item", autos.size() == 1 ? autos.get(0) : null);
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/auto_save.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Auto auto = new Auto();
        auto.toModel(toMapWithSingleValue(req.getParameterMap()));
        auto.setPicture(imageService.write(req.getPart("file")));
        autoService.save(auto);

        resp.sendRedirect(req.getContextPath() + "/catalog");
    }
}
