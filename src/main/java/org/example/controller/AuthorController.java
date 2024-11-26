package org.example.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.AuthorDAO;
import org.example.dto.AuthorGetAllRs;
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
    private AuthorService authorService;

    public void init(){
        authorService = new AuthorService(new AuthorDAO(new ConnectionUtil()));



    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        List<AuthorGetAllRs> all = authorService.getAll();

        String json = objectMapper.writeValueAsString(all);

        resp.getWriter().write(json);
    }
}
