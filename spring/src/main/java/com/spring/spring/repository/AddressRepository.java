package com.spring.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.spring.entity.AddressEntity;
import com.spring.spring.entity.StudentEntity;
import java.util.Optional;
//método usado para buscar o endereço associado a um estudante específico
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByStudent(StudentEntity student);
}
