import org.example.dao.AuthorDAO;
import org.example.model.Author;
import org.example.model.Book;
import org.example.util.ConnectionUtil;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorDaoTest {
    @Mock
    private ConnectionUtil connectionUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private AuthorDAO authorDAO; // Замените на ваш класс, где находится метод getAll()

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        when(connectionUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        authorDAO = new AuthorDAO(connectionUtil); // Замените на ваш класс
    }

    @Test
    public void testGetAll() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false); // возвратит true первый раз, затем false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Александр Дюма");

        List<Author> authors = authorDAO.getAll();

        assertFalse(authors.isEmpty());
        assertEquals(1, authors.size());
        assertEquals("Александр Дюма", authors.get(0).getName());
    }
}
