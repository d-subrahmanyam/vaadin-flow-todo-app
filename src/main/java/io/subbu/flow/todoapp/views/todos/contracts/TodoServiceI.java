package io.subbu.flow.todoapp.views.todos.contracts;

import io.subbu.flow.todoapp.views.todos.models.TodoModel;

import java.util.List;

/**
 * This interface defines the contract to be
 * implemented by the TodoService
 */
public interface TodoServiceI {

    /**
     * This method saves a TodoModel ot the repo
     * @param todoModel TodoModel
     * @return TodoModel
     */
    TodoModel save(TodoModel todoModel);

    /**
     * This method returns a collection of all the
     * available TodoModel from the repo
     * @return List&lt;TodoModel&gt;
     */
    List<TodoModel> list();

    /**
     * This method removes a TodoModel from the
     * repo given the id
     * @param id String
     */
    void remove(String id);

    /**
     * This method toggles TodoModel in the
     * repo given the id
     * @param id
     */
    void toggle(String id);
}
