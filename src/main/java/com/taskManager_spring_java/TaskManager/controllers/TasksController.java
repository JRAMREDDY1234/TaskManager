package com.taskManager_spring_java.TaskManager.controllers;

import com.taskManager_spring_java.TaskManager.dtos.CreateTaskDTO;
import com.taskManager_spring_java.TaskManager.dtos.ErrorResponseDTO;
import com.taskManager_spring_java.TaskManager.dtos.UpdateTaskDTO;
import com.taskManager_spring_java.TaskManager.entities.TaskEntity;
import com.taskManager_spring_java.TaskManager.repositories.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("")
    public ResponseEntity<List<TaskEntity>> getTasks(){
        var tasks = taskService.getTasks();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable("id") Integer id){
        var task = taskService.getTaskById(id);
        if(task == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping("")
    public ResponseEntity<TaskEntity> addTask(@RequestBody CreateTaskDTO body) throws ParseException {
        var task = taskService.addTask(body.getTitle(), body.getDescription(), body.getDeadline());

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") Integer id, @RequestBody UpdateTaskDTO body) throws ParseException{
        var task = taskService.updateTask(id, body.getDescription(), body.getDeadline(), body.getCompleted());

        if(task ==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception e){
        if(e instanceof ParseException){
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid Date format"));
        }
        e.printStackTrace();
        return ResponseEntity.internalServerError().body((new ErrorResponseDTO("Internal Server Error")));
    }
}
