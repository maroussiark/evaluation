package com.project.controller;

import java.sql.Connection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.config.User;
import com.project.connection.Connector;
import com.project.modele.Admin;
import com.project.modele.Client;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/")
public class LogController {

    @GetMapping("/profil")
    public String chooseProfil() {
        return "profil";
    }

    @GetMapping
    public String formLoginClient() {
        return "login-client";
    }

    @GetMapping("/login-form/admin")
    public String formLoginAdmin() {
        return "login-admin";
    }

    @PostMapping("/login-admin")
    public String loginAdmin(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
            Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Admin admin = new Admin(username, password, "");

            User user = admin.log(connection);
            request.getSession().setAttribute("user", user);
            connection.close();
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            Connector.CloseConnection(connection);
            model.addAttribute("erreur", e.getMessage());
            return "login-admin";
        }
    }

    @PostMapping("/login-client")
    public String loginClient(@RequestParam String username, HttpServletRequest request,
            Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            Client client = new Client(username);

            User user = client.log(connection);
            request.getSession().setAttribute("user", user);

            Connector.CloseConnection(connection);
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            Connector.CloseConnection(connection);
            model.addAttribute("erreur", e.getMessage());
            return "login-client";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";

    }

    @GetMapping("/error403")
    public String unauthorized() {
        return "error-403";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("page", "home");
        model.addAttribute("title", "");
        return "index";
    }

}
