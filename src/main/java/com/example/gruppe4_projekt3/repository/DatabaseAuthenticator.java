package com.example.gruppe4_projekt3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.AuthenticationException;
import javax.sql.DataSource;
import java.sql.Connection;

public class DatabaseAuthenticator {
    @Repository
    public class DatabaseAuthenticator {
        private static DataSource dataSource;

        @Autowired
        public void setDataSource(DataSource dataSource) {
            DatabaseAuthenticator.dataSource = dataSource;
        }

        // Udfører autentificering ved at validere brugernavn og adgangskode. Brugt i sammenhæng med at sikrer at brugere logger korrekt ind
        public static int authenticate(String username, String password) throws AuthenticationException {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
}
