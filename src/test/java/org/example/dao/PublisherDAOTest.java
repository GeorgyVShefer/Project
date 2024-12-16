package org.example.dao;

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
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PublisherDAOTest {
    private PostgreSQLContainer<?> postgreSQLContainer;
    private ConnectionUtil connectionUtil;
    private PublisherDAO publisherDAO;

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

        // Инициализируем DAO
        publisherDAO = new PublisherDAO(connectionUtil);

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
    public void getAll() {
        List<Publisher> allPub = publisherDAO.getAll();

        Publisher publisher1 = allPub.get(0);
        assertEquals("Publisher1", publisher1.getName());
        assertEquals("Book1", publisher1.getBooks().get(0).getTitle());
        assertEquals("Book3", publisher1.getBooks().get(1).getTitle());
        Publisher publisher2 = allPub.get(1);
        assertEquals("Publisher2", publisher2.getName());
        assertEquals("Book2", publisher2.getBooks().get(0).getTitle());
    }

    @Test
    public void getById() {
        Publisher publisher = publisherDAO.getById(1);

        assertEquals(1, publisher.getId());
        assertEquals("Publisher1", publisher.getName());
    }

    @Test
    public void save() {
        Publisher publisher = new Publisher(3, "pub3", new ArrayList<>());

        Publisher savePub = publisherDAO.save(publisher);

        assertEquals(3, savePub.getId());
        assertEquals("pub3", savePub.getName());
    }

    @Test
    public void update() {
        Publisher updatePublisher = new Publisher();
        updatePublisher.setName("updatePub");
        Publisher publisher = publisherDAO.update(1, updatePublisher);

        assertEquals("updatePub", publisher.getName());
    }

    @Test
    public void deleteIsSuccess() {
        boolean b = publisherDAO.deleteById(1);
        assertEquals(true,b);

    }

    @Test
    public void deleteIsFail() {
        boolean b = publisherDAO.deleteById(5);
        assertEquals(false,b);
    }
}
