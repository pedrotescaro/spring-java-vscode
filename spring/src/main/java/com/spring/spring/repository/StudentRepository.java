package com.spring.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.spring.entity.StudentEntity;

@Repository
//interface usado como um ponto de acesso ao banco de dados para a entidade StudentEntity
// Função Principal: Manipula dados de estudantes (CRUD básico)
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    boolean existsByEmail(String email);
}