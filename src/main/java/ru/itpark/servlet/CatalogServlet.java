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
import ru.itpark.service.AutoService;

public class CatalogServlet extends HttpServlet {

    private AutoService service;

    @Override
    public void init() {
        try {
            var context = new InitialContext();
            service = (AutoService) context.lookup("java:/comp/env/bean/auto-service");

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
                var autos = service.getAutoByParams(Map.of("id", split[1]));
                req.setAttribute("item", autos.size() == 1 ? autos.get(0) : null);
                req.getRequestDispatcher("/WEB-INF/jsp/details.jsp").forward(req, resp);
                return;
            }
        }

        Map<String, Object> params = toMapWithSingleValue(req.getParameterMap());
        req.setAttribute("items", service.getAutoByParams(params));
        req.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(req, resp);
    }

}

