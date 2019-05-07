package ru.itpark.servlet;

import static ru.itpark.utils.DBUtils.toMapWithSingleValue;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import ru.itpark.service.CSVService;

public class CSVServlet extends HttpServlet {

    private CSVService service;

    @Override
    public void init() {
        try {
            var context = new InitialContext();
            service = (CSVService) context.lookup("java:/comp/env/bean/csv-service");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"auto_catalog.csv\"");
        service.generateFile(response.getOutputStream(),
                toMapWithSingleValue(request.getParameterMap()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Part file = req.getPart("file_csv");
        service.saveByCsv(file);
        resp.sendRedirect(req.getContextPath() + "/catalog");
    }
}
