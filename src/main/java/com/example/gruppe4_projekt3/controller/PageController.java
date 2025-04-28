package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    // Viser hjemmesiden (index)
    @GetMapping("/")
    public String showIndexPage() {
        return "Homepage/index"; // Matcher templates/Homepage/index.html
    }

    // Viser login-siden
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Antager, at login.html ligger i templates/login.html
    }

    // Viser index-siden efter registrering
    @GetMapping("/auth")
    public String showAuthPage() {
        return "Homepage/index"; // Matcher templates/Homepage/index.html
    }
}