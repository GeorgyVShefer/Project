package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.ConnectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookDAOTest {
    private PostgreSQLContainer<?> postgreSQLContainer;
    private ConnectionUtil connectionUtil;
    private BookDAO bookDAO;

    @Before
    public void setUp() throws Exception {
        // Поднимаем контейнер с PostgreSQL
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgreSQLContainer.start();

        // Настраиваем ConnectionUtil для подключения к тестовой базе данных
        connectionUtil = new ConnectionUtil() {
            @Override
            public Connection getConnection() {
                try {
                    Class.forName("org.postgresql.Driver");
                    return DriverManager.getConnection(
                            postgreSQLContainer.getJdbcUrl(),
                            postgreSQLContainer.getUsername(),
                            postgreSQLContainer.getPassword()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        bookDAO = new BookDAO(connectionUtil);
        // Создаем таблицы и добавляем тестовые данные
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE authors (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255))");

            statement.execute("CREATE TABLE publishers (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255))");

            statement.execute("CREATE TABLE books (" +
                    "book_id SERIAL PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "author_id INT, " +
                    "publisher_id INT, " +
                    "publication_year DATE, " +
                    "FOREIGN KEY (author_id) REFERENCES authors(id), " +
                    "FOREIGN KEY (publisher_id) REFERENCES publishers(id))");

            statement.execute("INSERT INTO authors (name) VALUES " +
                    "('Author1'), ('Author2')");

            statement.execute("INSERT INTO publishers (name) VALUES " +
                    "('Publisher1'), ('Publisher2')");

            statement.execute("INSERT INTO books (title, author_id, publisher_id, publication_year) VALUES " +
                    "('Book1', 1, 1, '2023-01-01'), " +
                    "('Book2', 1, 2, '2022-01-01'), " +
                    "('Book3', 2, 1, '2021-01-01')");
        }
    }
    @BeforeEach
    public void clearDatabase() throws Exception {
        // Очищаем таблицы перед каждым тестом
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM books");
            statement.execute("DELETE FROM publishers");
            statement.execute("DELETE FROM authors");
        }

        // Добавляем тестовые данные
        try (Connection connection = connectionUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO authors (name) VALUES " +
                    "('Author1'), ('Author2')");

            statement.execute("INSERT INTO publishers (name) VALUES " +
                    "('Publisher1'), ('Publisher2')");

            statement.execute("INSERT INTO books (title, author_id, publisher_id, publication_year) VALUES " +
                    "('Book1', 1, 1, '2023-01-01'), " +
                    "('Book2', 1, 2, '2022-01-01'), " +
                    "('Book3', 2, 1, '2021-01-01')");
        }
    }

    @Test
    public void testGetAll(){
        List<Book> allBooks = bookDAO.getAll();

        assertEquals(3, allBooks.size());

        Book bookOne = allBooks.get(0);
        Book bookTwo = allBooks.get(1);
        Book bookThree = allBooks.get(2);

        assertEquals(1,allBooks.get(0).getId());
        assertEquals("Book1", allBooks.get(0).getTitle());
        assertEquals(2,allBooks.get(1).getId());
        assertEquals("Book2",allBooks.get(1).getTitle());
        assertEquals(3,allBooks.get(2).getId());
        assertEquals("Book3",allBooks.get(2).getTitle());
    }

    @Test
    public void testById(){
        Book bookById = bookDAO.getBookById(1);

        assertEquals("Book1", bookById.getTitle());
        assertEquals(1, bookById.getId());
    }

    @Test
    public void testSave(){
        Author author = new Author(1, "auth1", new ArrayList<>());
        Publisher publisher = new Publisher(1, "pub1", new ArrayList<>());
        Date date = new Date();
        Book newBook = new Book(4, "some title", author, publisher, date);
        Book saveBook = bookDAO.save(newBook);

        assertEquals(4, saveBook.getId());
        assertEquals("some title", saveBook.getTitle());
        assertEquals(1, saveBook.getAuthorId().getId());
        assertEquals(1,saveBook.getPublisherId().getId());
    }

    @Test
    public void testUpdate(){
        Author author = new Author(1, "auth1", new ArrayList<>());
        Publisher publisher = new Publisher(1, "pub1", new ArrayList<>());
        Date date = new Date();

        Book updateBook = new Book();
        updateBook.setTitle("some title");
        updateBook.setAuthorId(author);
        updateBook.setPublisherId(publisher);
        updateBook.setPublicationYear(date);

        Book saveBook = bookDAO.save(updateBook);

        assertEquals("some title", saveBook.getTitle());
        assertEquals(1, saveBook.getAuthorId().getId());
        assertEquals(1, saveBook.getPublisherId().getId());
    }

    @Test
    public void testDeleteIsSuccess(){
        boolean delete = bookDAO.delete(1);
        assertEquals(true, delete);

    }
    @Test
    public void testDeleteIsFail(){
        boolean delete = bookDAO.delete(5);
        assertEquals(false, delete);

    }
}
