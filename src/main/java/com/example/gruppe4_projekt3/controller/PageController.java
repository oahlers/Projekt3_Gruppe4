package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.repository.CarRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class PageController {

    @Autowired
    CarRepository carRepository carRepo;

    @GetMapping("")
    public String index(Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "index";
    }
}
