package org.example.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.BookDAO;
import org.example.dto.BookGetAllRs;
import org.example.model.Author;
import org.example.model.Book;
import org.example.service.BookService;
import org.example.util.ConnectionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class BookController extends HttpServlet {
    private BookService service;

    @Override
    public void init() throws ServletException {
        service = new BookService(new BookDAO(new ConnectionUtil()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        List<BookGetAllRs> allRespBook = service.getAll();

        String json = objectMapper.writeValueAsString(allRespBook);

        resp.getWriter().write(json);
    }
}
