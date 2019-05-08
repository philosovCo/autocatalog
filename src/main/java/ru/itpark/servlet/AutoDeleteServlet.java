package ru.itpark.servlet;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.itpark.service.AutoService;

public class AutoDeleteServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] split = pathInfo.split("/");
            if (split.length == 2) {
                service.delete(split[1]);
            }
        }
        resp.sendRedirect(req.getContextPath());
    }
}
