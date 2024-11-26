package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.ConnectionUtil;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorDAO {
    private ConnectionUtil connectionUtil;

    public AuthorDAO(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

   /* public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();
        Map<Integer, Author> authorMap = new HashMap<>();

        String authorQuery = "SELECT * FROM authors";
        String bookQuery = "SELECT * FROM books";

        try (Connection connection = connectionUtil.getConnection()) {
            // выполнение запроса на авторов
            try (PreparedStatement preparedStatement = connection.prepareStatement(authorQuery);
                 ResultSet authorRs = preparedStatement.executeQuery()) {
                while (authorRs.next()) {
                    int authorId = authorRs.getInt("id");
                    String authorName = authorRs.getString("name");
                    Author author = new Author();
                    author.setId(authorId);
                    author.setName(authorName);
                    author.setBooks(new ArrayList<>());

                    authors.add(author);
                    authorMap.put(authorId, author);
                }
            }

            // выполнение запроса на книги
            try (PreparedStatement preparedStatement = connection.prepareStatement(bookQuery);
                 ResultSet bookRs = preparedStatement.executeQuery()) {
                while (bookRs.next()) {
                    int bookId = bookRs.getInt("book_id");
                    String title = bookRs.getString("title");
                    int authorId = bookRs.getInt("author_id");
                    LocalDate publicationYear = bookRs.getDate("publication_year").toLocalDate();

                    Book book = new Book();
                    book.setId(bookId);
                    book.setTitle(title);
                    book.setPublicationYear(publicationYear);

                    Author author = authorMap.get(authorId);
                    if (author != null) {
                        // убедитесь, что это setAuthor, а не setAuthorId
                        book.setAuthorId(author);
                        author.getBooks().add(book);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving authors and books", e);
        }

        return authors;
    }*/

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

                books.add(new Book(bookId, title, getAuthorById(id), getPublisherById(publisherId), publicYear));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    public Author getAuthorById(int id) {
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

    public Publisher getPublisherById(int id){
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
}
