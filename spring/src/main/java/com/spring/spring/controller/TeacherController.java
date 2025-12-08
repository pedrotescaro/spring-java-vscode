package com.spring.spring.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.spring.entity.TeacherEntity;
import com.spring.spring.service.TeacherService;

@RestController
@RequestMapping("/api/v1/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<TeacherEntity> getAll() {
        return teacherService.getTeachers();
    }

    @GetMapping("/{id}")
    public Optional<TeacherEntity> getById(@PathVariable("id") Long id) {
        return teacherService.getTeacher(id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveTeacher(@RequestBody TeacherEntity teacher) {
        if (teacher.getId() == null) {
            if (teacherService.existsByEmail(teacher.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            Optional<TeacherEntity> existing = teacherService.getTeacher(teacher.getId());
            if (existing.isPresent()) {
                TeacherEntity current = existing.get();
                if (!current.getEmail().equals(teacher.getEmail()) &&
                    teacherService.existsByEmail(teacher.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }
        }

        teacherService.saveOrUpdate(teacher);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        Object idObj = request.get("id");
        Long teacherId = idObj != null ? Long.valueOf(idObj.toString()) : null;

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.ok().build();
        }

        boolean emailExists = teacherService.existsByEmail(email);

        if (emailExists && teacherId != null) {
            Optional<TeacherEntity> existing = teacherService.getTeacher(teacherId);
            if (existing.isPresent() && email.equals(existing.get().getEmail())) {
                return ResponseEntity.ok().build();
            }
        }

        if (emailExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable("id") Long id) {
        teacherService.delete(id);
    }
}
