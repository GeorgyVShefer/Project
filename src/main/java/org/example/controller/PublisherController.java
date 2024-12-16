package org.example.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.PublisherDAO;
import org.example.dto.*;
import org.example.mapper.PublisherMapper;
import org.example.model.Publisher;
import org.example.service.PublisherService;
import org.example.util.ConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/publishers")
public class PublisherController extends HttpServlet {
    private PublisherService service;

    @Override
    public void init() throws ServletException {
        service = new PublisherService(new PublisherDAO(new ConnectionUtil()), new PublisherMapper());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if(action.equals("getAll")){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();

            List<PublisherGetAllRs> all = service.getAll();

            String json = objectMapper.writeValueAsString(all);

            resp.getWriter().write(json);
        } else if(action.equals("getById")){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();

            PublisherGetByIdRs id = service.getById(Integer.parseInt(req.getParameter("id")));

            String json = objectMapper.writeValueAsString(id);

            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        PublisherSaveRq publisherSaveRq = objectMapper.readValue(req.getReader(), PublisherSaveRq.class);


        service.save(publisherSaveRq);

        String json = objectMapper.writeValueAsString(publisherSaveRq);

        resp.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        PublisherUpdateRq publisherUpdateRq = objectMapper.readValue(req.getReader(), PublisherUpdateRq.class);

        service.update(Integer.parseInt(req.getParameter("id")), publisherUpdateRq);

        String json = objectMapper.writeValueAsString(publisherUpdateRq);

        resp.getWriter().write(json);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        String id = req.getParameter("id");
        String delete = service.delete(Integer.parseInt(id));

        resp.getWriter().write(delete);
    }
}
