package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {
    private ConnectionUtil connectionUtil;

    public PublisherDAO(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    public List<Publisher> getAll() {
        List<Publisher> publishers = new ArrayList<>();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from publishers")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                publishers.add(new Publisher(id, name, getAllBooksById(id)));
            }
            return publishers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Publisher getById(int id){
        Publisher publisher = new Publisher();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from publishers where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setBooks(getAllBooksById(id));
            }
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Publisher save(Publisher publisher){
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into publishers(id,name) values (?,?)")){
            statement.setInt(1, publisher.getId());
            statement.setString(2,publisher.getName());
            statement.executeUpdate();

            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id){
        deleteBookByPublisherId(id, false);

        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from publishers where id = ?")){
            statement.setInt(1, id);

            int i = statement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Publisher update(int id, Publisher publisher){
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("update publishers set name = ? where id = ? ")){
            statement.setString(1, publisher.getName());
            statement.setInt(2,id);
            statement.executeUpdate();
            return publisher;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> getAllBooksById(int id) {
        List<Book> books = new ArrayList<>();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from books where publisher_id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                Date publicationYear = resultSet.getDate("publication_year");
                books.add(new Book(bookId, title, getAuthorById(authorId), getPublisherById(id), publicationYear));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Author getAuthorById(int id) {
        Author author = new Author();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from authors where id = ?")) {
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

    private Publisher getPublisherById(int id) {
        Publisher publisher = new Publisher();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from publishers where id = ?")) {
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

    private void deleteBookByPublisherId(int id, boolean isRecursiveCall){
        if (isRecursiveCall) {
            return;
        }
        try(Connection connection = connectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from books where publisher_id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
