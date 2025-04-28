package com.example.gruppe4_projekt3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


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
        return "Homepage/login"; // Antager, at login.html ligger i templates/login.html
    }

    // Viser index-siden efter registrering
    @GetMapping("/auth")
    public String showAuthPage() {
        return "Homepage/index"; // Matcher templates/Homepage/index.html
    }
}