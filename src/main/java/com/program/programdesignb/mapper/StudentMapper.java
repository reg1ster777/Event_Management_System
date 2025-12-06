package com.program.programdesignb.mapper;

import com.program.programdesignb.domain.Student;
import java.util.List;

public interface StudentMapper {
    Student findById(Long id);

    List<Student> findAll();

    int insert(Student student);

    int update(Student student);

    int delete(Long id);
}
