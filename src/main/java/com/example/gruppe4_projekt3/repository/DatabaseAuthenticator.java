package com.example.gruppe4_projekt3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.AuthenticationException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/* ((Kan være vi skal bruge den når vi hoster))
public class DatabaseAuthenticator {
    @Repository
    public class databaseAuthenticator {
        private static DataSource dataSource;

        @Autowired
        public void setDataSource(DataSource dataSource) {
            databaseAuthenticator.dataSource = dataSource;
        }

        public static int authenticate(String username, String password) throws AuthenticationException, SQLException {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
            }
            return 0;
        }
    }
}

 */
