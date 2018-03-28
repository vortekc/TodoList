package com.todo.ToDoList.db.repository;

import com.todo.ToDoList.db.data.TaskItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TasksRepository extends CrudRepository<TaskItem, Long> {
    List<TaskItem> findByDescriptionIgnoreCaseContaining(String search);
    List<TaskItem> findAll();
    void deleteAllByIsDone(boolean isDone);
    Optional<TaskItem> findByDescription(String name);
}
