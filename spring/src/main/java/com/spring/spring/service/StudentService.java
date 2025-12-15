package com.spring.spring.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.spring.entity.StudentEntity;
import com.spring.spring.repository.AddressRepository;
import com.spring.spring.repository.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<StudentEntity> getStudents(){
        return studentRepository.findAll();
    }

    public Optional<StudentEntity> getStudent(Long id) {
        Long studentId = id;
        if (studentId != null) {
            return studentRepository.findById(studentId);
        }
        return Optional.empty();
    }

    public void saveOrUpdate(StudentEntity student) {
        StudentEntity studentToSave = student;
        if (studentToSave != null) {
            studentRepository.save(studentToSave);
        }
    }

    @Transactional
    public void delete(Long id) {
        Long studentId = id;
        if (studentId != null) {
            Optional<StudentEntity> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                addressRepository.deleteByStudent(student.get());
                studentRepository.deleteById(studentId);
            }
        }
    }

    public boolean existsByEmail(String email) {
        String emailToCheck = email;
        if (emailToCheck != null) {
            return studentRepository.existsByEmail(emailToCheck);
        }
        return false;
    }
}
