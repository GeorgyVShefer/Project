package org.example.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.AuthorDAO;
import org.example.dto.AuthorGetAllRs;
import org.example.dto.AuthorGetByIdRs;
import org.example.dto.AuthorSaveRq;
import org.example.mapper.AuthorMapper;
import org.example.service.AuthorService;
import org.example.util.ConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/authors")
public class AuthorController extends HttpServlet {
    AuthorService authorService;

    public void init(){
        authorService = new AuthorService(new AuthorDAO(new ConnectionUtil()), new AuthorMapper());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("getAll".equals(action)) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            List<AuthorGetAllRs> all = authorService.getAll();

            String json = objectMapper.writeValueAsString(all);

            resp.getWriter().write(json);
        }else if("getById".equals(action)){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            int id = Integer.parseInt(req.getParameter("id"));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            AuthorGetByIdRs authorById = authorService.getAuthorById(id);

            String json = objectMapper.writeValueAsString(authorById);

            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        //Достать json объект из метода post класса java servlet
        ObjectMapper objectMapper = new ObjectMapper();

        AuthorSaveRq authorSaveRq = objectMapper.readValue(req.getReader(), AuthorSaveRq.class);

        authorService.save(authorSaveRq);

        String json = new ObjectMapper().writeValueAsString(authorSaveRq);

        resp.getWriter().write(json);
    }
}
