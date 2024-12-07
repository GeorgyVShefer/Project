package org.example.dao;


import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.DriverManager;



class ConnectionUtilTest extends ConnectionUtil {
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    public ConnectionUtilTest(String URL, String USERNAME, String PASSWORD) {
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public Connection getAllConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}