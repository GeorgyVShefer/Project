package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private ConnectionUtil connectionUtil;

    public BookDAO(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from books")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int publisherId = resultSet.getInt("publisher_id");
                Date publicYear = resultSet.getDate("publication_year");

                books.add(new Book(id, title, getAuthorById(authorId), getPublisherById(publisherId), publicYear));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getBookById(int id) {
        Book book = new Book();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select  * from books where book_id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorId(getAuthorById(resultSet.getInt("author_id")));
                book.setPublisherId(getPublisherById(resultSet.getInt("publisher_id")));
                book.setPublicationYear(resultSet.getDate("publication_year"));
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Author getAuthorById(int id) {
        Author author = new Author();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id,name from authors where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
            }
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Publisher getPublisherById(int id) {
        Publisher publisher = new Publisher();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id,name from publishers where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
            }
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
