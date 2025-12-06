package com.program.programdesignb.controller;

import com.program.programdesignb.api.Result;
import com.program.programdesignb.domain.Student;
import com.program.programdesignb.service.StudentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Result<List<Student>> list() {
        return Result.success(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Student>> get(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.fail(404, "Student not found"));
        }
        return ResponseEntity.ok(Result.success(student));
    }

    @PostMapping
    public ResponseEntity<Result<Student>> create(@RequestBody Student student) {
        studentService.create(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success("created", student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Student>> update(@PathVariable Long id, @RequestBody Student student) {
        Student existing = studentService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.fail(404, "Student not found"));
        }
        Student updated = studentService.update(id, student);
        return ResponseEntity.ok(Result.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
        Student existing = studentService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.fail(404, "Student not found"));
        }
        studentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Result.success(null));
    }
}
