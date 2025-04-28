package com.example.gruppe4_projekt3.controller;

import com.example.gruppe4_projekt3.model.Car;
import com.example.gruppe4_projekt3.model.Employee;
import com.example.gruppe4_projekt3.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/")
public class PageController {

    @Autowired
    private CarRepository carRepo;

    // Viser forsiden og tilføjer den aktuelle bruger til modellen
    @GetMapping("")
    public String mainPage(HttpSession session, Model model) {
        // Henter den aktuelle bruger (Employee) fra sessionen
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "index"; // Returnerer forsiden (index.html)
    }

    // Viser listen af biler og henter alle biler fra databasen
    @GetMapping("/showCars")
    public String showCarListPage(HttpSession session, Model model) {
        // Henter den aktuelle bruger (Employee) fra sessionen
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        model.addAttribute("loggedInEmployee", loggedInEmployee);

        // Henter alle biler fra car-repository
        List<Car> carList = carRepo.getAllCars(); // Antager, at getAllCars findes i CarRepository
        model.addAttribute("carList", carList); // Tilføjer bilerne til modellen
        return "showCars"; // Returnerer "showCars.html"
    }

    // Ekstra indgang til forsiden (kan være nyttigt i visse tilfælde)
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "index"; // Returnerer forsiden (index.html)
    }
}
