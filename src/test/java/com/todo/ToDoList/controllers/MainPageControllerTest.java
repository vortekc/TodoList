package com.todo.ToDoList.controllers;

import com.todo.ToDoList.db.data.TaskItem;
import com.todo.ToDoList.db.repository.TasksRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainPageControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TasksRepository tasksRepository;
    private List<TaskItem> taskItems = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        taskItems.clear();
        tasksRepository.deleteAll();

        TaskItem taskItem = new TaskItem();
        taskItem.setDescription("Airbus");
        taskItems.add(taskItem);
        tasksRepository.save(taskItem);

        taskItem = new TaskItem();
        taskItem.setDescription("Boeing");
        taskItems.add(taskItem);
        tasksRepository.save(taskItem);

        taskItem = new TaskItem();
        taskItem.setDescription("Bombardier");
        taskItems.add(taskItem);
        tasksRepository.save(taskItem);
    }

    @Test
    public void mainPageLoadingTest() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("tasklist", taskItems));
    }

    @Test
    public void searchTest() throws Exception {
        List<TaskItem> testResultList = new ArrayList();
        TaskItem taskItem =  tasksRepository.findByDescription("Boeing").orElseThrow(() -> new Exception());

        testResultList.add(taskItem);

        mvc.perform(get("/?search=Boeing"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("tasklist", testResultList));
    }

    @Test
    public void changeTaskStatusTest() throws Exception {
        TaskItem taskItem =  tasksRepository.findByDescription("Boeing").orElseThrow(() -> new Exception());
        mvc.perform(get("/"+taskItem.getId()+"/true")).andExpect(status().isFound());

        assertTrue(tasksRepository.findByDescription("Boeing").get().isDone());
        assertFalse(tasksRepository.findByDescription("Airbus").get().isDone());
        assertFalse(tasksRepository.findByDescription("Bombardier").get().isDone());
    }

    @Test
    public void changeTaskNotFoundTest() throws Exception {
        mvc.perform(get("/2018/true")).andExpect(status().isNotFound());
    }

    @Test
    public void clearCompletedTasksTest() throws Exception {
        TaskItem boeingTaskItem =  tasksRepository.findByDescription("Boeing").orElseThrow(() -> new Exception());
        TaskItem bombardierTaskItem =  tasksRepository.findByDescription("Bombardier").orElseThrow(() -> new Exception());

        mvc.perform(get("/"+boeingTaskItem.getId()+"/true")).andExpect(status().isFound());
        mvc.perform(get("/"+bombardierTaskItem.getId()+"/true")).andExpect(status().isFound());

        mvc.perform(get("/clear")).andExpect(status().isFound());

        assertTrue(tasksRepository.findAll().size() == 1);
    }
}