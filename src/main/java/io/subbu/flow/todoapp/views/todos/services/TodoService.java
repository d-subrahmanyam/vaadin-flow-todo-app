package io.subbu.flow.todoapp.views.todos.services;

import io.subbu.flow.todoapp.views.todos.contracts.TodoServiceI;
import io.subbu.flow.todoapp.views.todos.models.TodoModel;
import io.subbu.flow.todoapp.views.todos.repos.TodoRepo;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SpringComponent
public class TodoService implements TodoServiceI {

    @Autowired
    private TodoRepo todoRepo;

    /**
     * This method saves a TodoModel ot the repo
     *
     * @param todoModel TodoModel
     * @return TodoModel
     */
    @Override
    public TodoModel save(TodoModel todoModel) {
        return todoRepo.save(todoModel);
    }

    /**
     * This method returns a collection of all the
     * available TodoModel from the repo
     *
     * @return List&lt;TodoModel&gt;
     */
    @Override
    public List<TodoModel> list() {
        return todoRepo.findAll();
    }

    /**
     * This method removes a TodoModel from the
     * repo given the id
     *
     * @param id String
     */
    @Override
    public void remove(String id) {
        todoRepo.deleteById(id);
    }

    /**
     * This method toggles TodoModel in the
     * repo given the id
     *
     * @param id
     */
    @Override
    public void toggle(String id) {
        TodoModel todoModel = todoRepo.findById(id).get();
        boolean isCompleted = todoModel.isCompleted();
        todoModel.setCompleted(!isCompleted);
        todoRepo.save(todoModel);
    }
}
