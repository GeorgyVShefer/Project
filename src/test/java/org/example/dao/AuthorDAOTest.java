package org.example.dao;

import org.example.model.Author;
import org.example.util.ConnectionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AuthorDAOTest {

    private PostgreSQLContainer<?> postgreSQLContainer;
    private ConnectionUtil connectionUtil;
    private AuthorDAO authorDAO;

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
        authorDAO = new AuthorDAO(connectionUtil);

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
    public void testGetAll() {
        // Вызываем метод DAO
        List<Author> authors = authorDAO.getAll();

        // Проверяем результат
        assertEquals(2, authors.size());

        // Проверяем данные первого автора
        Author author1 = authors.get(0);
        assertEquals("Author1", author1.getName());
        assertEquals(2, author1.getBooks().size());
        assertEquals("Book1", author1.getBooks().get(0).getTitle());
        assertEquals("Book2", author1.getBooks().get(1).getTitle());

        // Проверяем данные второго автора
        Author author2 = authors.get(1);
        assertEquals("Author2", author2.getName());
        assertEquals(1, author2.getBooks().size());
        assertEquals("Book3", author2.getBooks().get(0).getTitle());
    }

    @Test
    public void testGetById() {
        Author authorById = authorDAO.getAuthorById(1);

        assertEquals(1, authorById.getId());
        assertEquals("Author1", authorById.getName());
        assertEquals("Book1", authorById.getBooks().get(0).getTitle());
        assertEquals("Book2", authorById.getBooks().get(1).getTitle());
    }



    @After
    public void tearDown() {
        // Останавливаем контейнер после тестов
        postgreSQLContainer.stop();
    }
}