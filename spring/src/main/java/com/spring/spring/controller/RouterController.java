/* Rota é um caminho que leva a algum recurso ou página;
   geralmente usado para renderizar views (HTML) */

package com.spring.spring.controller;

import com.spring.spring.dto.CepResponse;
import com.spring.spring.entity.AddressEntity;
import com.spring.spring.entity.StudentEntity;
import com.spring.spring.repository.AddressRepository;
import com.spring.spring.repository.StudentRepository;
import com.spring.spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
public class RouterController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/students")
    public String studentsPage() {
        return "students";
    }

    @GetMapping("/address")
    public String addressPage() {
        return "address";
    }
    
    private static final String API_BASE = "/api/v1";

    @GetMapping(API_BASE + "/addresses")
    @ResponseBody
    public List<AddressEntity> getAllAddresses() {
        return addressRepository.findAll();
    }

    @GetMapping(API_BASE + "/addresses/{id}")
    @ResponseBody
    public AddressEntity getAddressById(@PathVariable Long id) {
        return Optional.ofNullable(id)
                .flatMap(addressRepository::findById)
                .orElse(null);
    }

    @PostMapping(API_BASE + "/addresses")
    @ResponseBody
    public AddressEntity saveOrUpdateAddress(@RequestBody AddressEntity address) {
        Optional.ofNullable(address.getStudent())
                .map(StudentEntity::getStudentId)
                .flatMap(studentRepository::findById)
                .ifPresent(address::setStudent);
        return addressRepository.save(address);
    }

    @DeleteMapping(API_BASE + "/addresses/{id}")
    @ResponseBody
    public void deleteAddress(@PathVariable Long id) {
        Optional.ofNullable(id).ifPresent(addressRepository::deleteById);
    }

    @GetMapping(API_BASE + "/cep/{cep}")
    @ResponseBody
    public CepResponse getAddressByCep(@PathVariable String cep) {
        return viaCepService.getAddressByCep(cep);
    }
}
