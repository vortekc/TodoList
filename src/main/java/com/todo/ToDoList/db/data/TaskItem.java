package com.todo.ToDoList.db.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Size(min=1)
    private String description;
    private boolean isDone;

    public TaskItem() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskItem taskItem = (TaskItem) o;
        return getId() == taskItem.getId() &&
                isDone() == taskItem.isDone() &&
                Objects.equals(getDescription(), taskItem.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getDescription(), isDone());
    }

    @Override
    public String toString() {
        return "TaskItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
