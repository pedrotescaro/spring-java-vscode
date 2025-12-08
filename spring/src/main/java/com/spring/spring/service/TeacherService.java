package com.spring.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.spring.entity.TeacherEntity;
import com.spring.spring.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherEntity> getTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<TeacherEntity> getTeacher(Long id) {
        return Optional.ofNullable(id).flatMap(teacherRepository::findById);
    }

    public void saveOrUpdate(TeacherEntity teacher) {
        if (teacher != null) {
            teacherRepository.save(teacher);
        }
    }

    public void delete(Long id) {
        Optional.ofNullable(id).ifPresent(teacherRepository::deleteById);
    }

    public boolean existsByEmail(String email) {
        return email != null && teacherRepository.existsByEmail(email);
    }
}
