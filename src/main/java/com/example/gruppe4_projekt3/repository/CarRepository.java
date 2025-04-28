package com.example.gruppe4_projekt3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

public class CarRepository {
    @Repository

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
