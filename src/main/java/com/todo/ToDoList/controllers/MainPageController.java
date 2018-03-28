package com.todo.ToDoList.controllers;

import com.todo.ToDoList.db.data.TaskItem;
import com.todo.ToDoList.db.repository.TasksRepository;
import com.todo.ToDoList.errors.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class MainPageController {
    @Autowired
    TasksRepository tasksRepository;
    private final String MAIN_PAGE = "mainpage";
    private final String NEW_TASK_PAGE = "newtask";

    @GetMapping("/")
    public String mainPage(@RequestParam(value="search",required=false) String search, Model model) {
        List<TaskItem> items;
        if(search != null) {
            items = tasksRepository.findByDescriptionIgnoreCaseContaining(search);
        } else {
            items = tasksRepository.findAll();
        }

        model.addAttribute("tasklist", items);
        return MAIN_PAGE;
    }

    @GetMapping("/new")
    public String newTaskPage(TaskItem taskItem) {
        return NEW_TASK_PAGE;
    }

    @PostMapping("/new")
    public String  submitNewTask(@Valid TaskItem taskItem, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return NEW_TASK_PAGE;
        }

        if(taskItem != null) {
            tasksRepository.save(taskItem);
        }

        return "redirect:/";
    }

    @GetMapping("/{id}/{isdone}")
    public String changeTaskStatus(@PathVariable Long id, @PathVariable Boolean isdone) {
        Optional<TaskItem> taskItemOptional = tasksRepository.findById(id);
        if(taskItemOptional.isPresent()) {
            TaskItem taskItem = taskItemOptional.get();
            taskItem.setDone(isdone);
            tasksRepository.save(taskItem);
        } else {
            throw new TaskNotFoundException();
        }

        return "redirect:/";
    }

    @GetMapping("/clear")
    public String clearCompletedTasks() {
        tasksRepository.deleteAllByIsDone(true);
        return "redirect:/";
    }
}
