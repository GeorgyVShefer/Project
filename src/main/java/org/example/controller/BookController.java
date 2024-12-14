package org.example.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.BookDAO;
import org.example.dto.*;
import org.example.mapper.BookMapper;
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
        service = new BookService(new BookDAO(new ConnectionUtil()), new BookMapper());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action.equals("getById")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            String id = req.getParameter("id");

            BookGetByIdRs byId = service.getById(Integer.parseInt(id));

            String json = objectMapper.writeValueAsString(byId);

            resp.getWriter().write(json);
        } else if (action.equals("getAll")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            List<BookGetAllRs> allRespBook = service.getAll();

            String json = objectMapper.writeValueAsString(allRespBook);

            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        BookSaveRq bookSaveRq = objectMapper.readValue(req.getReader(), BookSaveRq.class);


        service.save(bookSaveRq);

        String json = objectMapper.writeValueAsString(bookSaveRq);

        resp.getWriter().write(json);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Book book = objectMapper.readValue(req.getReader(), Book.class);


        service.update(Integer.parseInt(req.getParameter("id")), book);

        String json = new ObjectMapper().writeValueAsString(book);

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
