package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    private ConnectionUtil connectionUtil;

    public AuthorDAO(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }


    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from authors")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                authors.add(new Author(id, name, getAllBooksByAuthorId(id)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authors;
    }

    public Author getAuthorById(int id){
        Author author = new Author();
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * from authors where id =?")){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setBooks(getAllBooksByAuthorId(id));
            }
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Author save(Author author){
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO authors(id,name) values (?,?)")){
            statement.setInt(1,author.getId());
            statement.setString(2,author.getName());
            statement.executeUpdate();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Author update(int id, Author author){
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE  authors SET name = ? where id = ?")){
            statement.setString(1, author.getName());
            statement.setInt(2, id);
            statement.executeUpdate();
            return author;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean deleteAuthorById(int id){
        deleteBookByAuthorId(id, false);

        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from authors where id = ?")){
            statement.setInt(1, id);

            int i = statement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Book> getAllBooksByAuthorId(int id) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from books where author_id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                int publisherId = resultSet.getInt("publisher_id");
                Date publicYear = resultSet.getDate("publication_year");

                books.add(new Book(bookId, title, getAuthorId(id), getPublisherById(publisherId), publicYear));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    private Author getAuthorId(int id) {
        Author author = new Author();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id, name from authors where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int authorId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                author.setId(authorId);
                author.setName(name);
            }
        return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Publisher getPublisherById(int id){
        Publisher publisher = new Publisher();

        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id, name from publishers where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
            }
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteBookByAuthorId(int id, boolean isRecursiveCall){
        if (isRecursiveCall) {
            return;
        }
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from books where author_id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
