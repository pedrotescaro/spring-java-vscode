// Define o pacote do controlador da aplicação
package com.spring.spring.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

// Importa a entidade Student, que será manipulada pelo controller
import com.spring.spring.entity.StudentEntity;
import com.spring.spring.service.StudentService;

// Importa o serviço que lida com listas e valores opcionais
import java.util.List;
import java.util.Optional;

// Importa as anotações para mapear endpoints HTTP
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;

// Inicia o classe como um REST controller do Spring
@RestController
@RequestMapping(path = "/api/v1/students")
@CrossOrigin(origins = "*")
public class StudentController {

    // Injeção automática de uma instância de StudentService
    @Autowired
    private StudentService studentService;

    // Endpoint GET para listar todos os estudantes
    @GetMapping
    public List<StudentEntity> getAll() {
        // Chama o serviço para buscar todos os estudantes e retorna como JSON
        return studentService.getStudents();
    }

    // Endpoint POST para criar ou atualizar um estudante
    @PostMapping("/register")
    public ResponseEntity<?> saveStudent(@RequestBody StudentEntity student) {
        // Verifica se o email já existe (exceto para o próprio estudante sendo atualizado)
        if (student.getStudentId() == null) {
            // Criando novo estudante - verifica se email já existe
            if (studentService.existsByEmail(student.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            // Atualizando estudante - verifica se email existe em outro estudante
            Optional<StudentEntity> existingStudent = studentService.getStudent(student.getStudentId());
            if (existingStudent.isPresent()) {
                StudentEntity current = existingStudent.get();
                // Se o email mudou, verifica se já existe em outro estudante
                if (!current.getEmail().equals(student.getEmail()) && 
                    studentService.existsByEmail(student.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }
        }
        
        // Recebe um objeto Student no corpo da requisição e envia para o serviço salvar/atualizar
        studentService.saveOrUpdate(student);
        return ResponseEntity.ok().build();
    }

    // Endpoint POST para verificar se o email já existe
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        Object studentIdObj = request.get("studentId");
        Long studentId = studentIdObj != null ? Long.valueOf(studentIdObj.toString()) : null;
        
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.ok().build();
        }
        
        boolean emailExists = studentService.existsByEmail(email);
        
        // Se estamos editando um estudante, verifica se o email pertence a outro estudante
        if (emailExists && studentId != null) {
            Optional<StudentEntity> existingStudent = studentService.getStudent(studentId);
            if (existingStudent.isPresent() && existingStudent.get().getEmail().equals(email)) {
                // O email pertence ao próprio estudante que está sendo editado
                return ResponseEntity.ok().build();
            }
        }
        
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        return ResponseEntity.ok().build();
    }

    // Endpoint DELETE para excluir um estudante pelo ID
    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable("studentId") long studentId) {
        // Chama o serviço para deletar o estudante com o ID fornecido
        studentService.delete(studentId);
    }

    // Endpoint GET para retornar o estudante pelo ID
    @GetMapping("/{studentId}")
    public Optional<StudentEntity> getById(@PathVariable("studentId") long studentId) {
        // Retorna o estudante correspondente ao ID, se existir
        return studentService.getStudent(studentId);
    }
}

