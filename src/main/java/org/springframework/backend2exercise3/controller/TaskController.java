package org.springframework.backend2exercise3.controller;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.springframework.backend2exercise3.service.TrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {

    @Autowired
    TrelloService trelloService;

    @GetMapping("/tasks/create")
    public String showCreateForm(Model model) {
        model.addAttribute("taskForm", new TaskForm());
        return "create-task"; // This Thymeleaf template
    }

    @PostMapping("/tasks/create")
    public String createTask(@Valid @ModelAttribute TaskForm taskForm,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-task";
        }

        try {
            // Call your Jira/Trello service here
            trelloService.createTask(taskForm);
            model.addAttribute("message", "Uppgift skapad framgångsrikt!");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Ett fel uppstod: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }

        model.addAttribute("taskForm", new TaskForm());
        return "create-task";
    }
}
