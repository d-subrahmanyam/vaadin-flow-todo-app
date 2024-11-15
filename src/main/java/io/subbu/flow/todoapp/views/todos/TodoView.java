package io.subbu.flow.todoapp.views.todos;

import io.subbu.flow.todoapp.views.todos.models.TodoModel;
import io.subbu.flow.todoapp.views.todos.services.TodoService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Smart Todos")
@Route("/todos")
@Menu(order = 1, icon = "line-awesome/svg/globe-solid.svg")
@SpringComponent
@Slf4j
public class TodoView extends VerticalLayout {

    private TextField todoActivity;
    private static Grid<TodoModel> todoModelGrid;
    private static final List<TodoModel> todoModels = new ArrayList<>();

    private final TodoService todoService;

    public TodoView(@Autowired TodoService todoService) {
        this.todoService = todoService;
        setupTodoInputView();
        setupTodoGridView();
    }

    /**
     * This method sets-up the Todo input view
     */
    private void setupTodoInputView() {
        todoActivity = new TextField("Todo Activity", "Enter a todo activity");
        todoActivity.setWidth("50%");
        Button addTodoBtn = new Button("Add Todo", new Icon(VaadinIcon.CHECK));
        addTodoBtn.setIconAfterText(true);
        addTodoBtn.addClickListener(e -> {
            TodoModel todoModel = todoService.save(TodoModel.builder().activity(todoActivity.getValue()).completed(false).build());
            Notification.show("Added, Todo with id - " + todoModel.getId());
            refreshGrid();
            todoActivity.setValue("");
        });
        addTodoBtn.addClickShortcut(Key.ENTER);

        setMargin(true);
        HorizontalLayout todoActivityLayout = new HorizontalLayout();
        todoActivityLayout.setWidth("100%");
        todoActivityLayout.setVerticalComponentAlignment(Alignment.END, todoActivity, addTodoBtn);
        todoActivityLayout.add(todoActivity);
        todoActivityLayout.add(addTodoBtn);

        add(todoActivityLayout);
    }

    /**
     * This method sets-up the Todo grid view
     */
    private void setupTodoGridView() {
        todoModelGrid = new Grid<>(TodoModel.class, false);
        todoModelGrid.setAllRowsVisible(true);
        todoModelGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        todoModelGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        Span activityHeader = new Span("Activity");
        activityHeader.getStyle().setFontWeight(Style.FontWeight.BOLD).setTextAlign(Style.TextAlign.CENTER);
        todoModelGrid.addColumn(
                new ComponentRenderer<>(Span::new, (span, todoModel) -> {
                    span.setText(todoModel.getActivity());
                    if(todoModel.isCompleted()) {
                        span.getStyle().set("text-decoration", "line-through");
                    }
                })).setHeader(activityHeader);

        Span completedHeader = new Span("Completed");
        completedHeader.getStyle().setFontWeight(Style.FontWeight.BOLD).setTextAlign(Style.TextAlign.CENTER);
        todoModelGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, todoModel) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON);
                    if(todoModel.isCompleted()) {
                        button.setIcon(new Icon(VaadinIcon.CHECK_SQUARE));
                    } else {
                        button.setIcon(new Icon(VaadinIcon.CHECK_SQUARE_O));
                    }
                    button.addClickListener(evt -> {
                        todoService.toggle(todoModel.getId());
                        Notification.show("Toggled, Todo with id - " + todoModel.getId());
                        refreshGrid();
                    });
                })).setHeader(completedHeader);

        Span manageHeader = new Span("Manage");
        manageHeader.getStyle().setFontWeight(Style.FontWeight.BOLD).setTextAlign(Style.TextAlign.CENTER);
        todoModelGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, todoModel) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(evt -> {
                        todoService.remove(todoModel.getId());
                        Notification.show("Removed, Todo with id - " + todoModel.getId());
                        refreshGrid();
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader(manageHeader);

        todoModels.addAll(todoService.list());
        todoModelGrid.setItems(todoModels);
        todoModelGrid.setEnabled(true);
        add(todoModelGrid);
    }

    /**
     * This method refreshes the contents of the grid
     */
    private void refreshGrid() {
        todoModels.clear();
        todoModels.addAll(todoService.list());
        todoModelGrid.getDataProvider().refreshAll();
    }
}
