package org.example.dao;

import org.example.model.Author;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Testcontainers
class AuthorDAOTest {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withDatabaseName("databasePostgres")
                    .withUsername("test")
                    .withPassword("testPass");

    private AuthorDAO authorDAO;
    private static ConnectionUtilTest connectionUtilTest;

    @BeforeAll
    public static void createConnection() throws Exception {
        try (Connection connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())) {
            String sqlQuery = "CREATE table author(id serial primary key, name varchar(50))";
            connection.createStatement().executeUpdate(sqlQuery);
        }
        connectionUtilTest =
                new ConnectionUtilTest(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
    }
    @BeforeEach
    public void setup() throws Exception{
        authorDAO = new AuthorDAO(connectionUtilTest);
        clearDatabase();
    }
    private void clearDatabase() throws Exception{
        try (Connection allConnection = connectionUtilTest.getAllConnection();
             PreparedStatement statement = allConnection.prepareStatement("delete from author")){
            statement.executeUpdate();
        }
    }
    @Test
    public void testGetAll_ShouldFindAllAuthors_WhenOurQueryIsSuccess() throws Exception{
        int id1 = 1;
        int id2 = 2;
        String name1 = "Толстой";
        String name2 = "Гоголь";

        try(Connection allConnection = connectionUtilTest.getAllConnection();
            PreparedStatement statement =
                    allConnection.prepareStatement("INSERT INTO author(id,name) values (?,?)")){
            statement.setInt(1, id1);
            statement.setString(2,name1);
            statement.executeUpdate();
            statement.setInt(1,id2);
            statement.setString(2,name2);
            statement.executeUpdate();
        }

        List<Author> all = authorDAO.getAll();

        assertEquals(name1, all.get(0).getName());
    }
}