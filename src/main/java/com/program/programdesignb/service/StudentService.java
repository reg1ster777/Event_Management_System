package com.program.programdesignb.service;

import com.program.programdesignb.domain.Student;
import com.program.programdesignb.mapper.StudentMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public List<Student> findAll() {
        return studentMapper.findAll();
    }

    public Student findById(Long id) {
        return studentMapper.findById(id);
    }

    public Student create(Student student) {
        studentMapper.insert(student);
        return student;
    }

    public Student update(Long id, Student student) {
        student.setId(id);
        studentMapper.update(student);
        return student;
    }

    public void delete(Long id) {
        studentMapper.delete(id);
    }
}
