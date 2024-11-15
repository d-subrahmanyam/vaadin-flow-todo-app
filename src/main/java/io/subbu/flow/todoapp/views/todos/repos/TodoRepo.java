package io.subbu.flow.todoapp.views.todos.repos;

import io.subbu.flow.todoapp.views.todos.models.TodoModel;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringComponent
public interface TodoRepo extends JpaRepository<TodoModel, String> {
}
