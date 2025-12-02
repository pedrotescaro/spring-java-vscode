package com.spring.spring.controller;

import com.spring.spring.entity.AddressEntity;
import com.spring.spring.service.AddressService;
import com.spring.spring.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private StudentService studentService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressEntity addressDetails) {
        return addressService.getAddress(id)
                .map(existingAddress -> {
                    return studentService.getStudent(addressDetails.getStudent().getStudentId())
                            .map(student -> {
                                existingAddress.setStudent(student);
                                existingAddress.setStreet(addressDetails.getStreet());
                                existingAddress.setCity(addressDetails.getCity());
                                existingAddress.setState(addressDetails.getState());
                                existingAddress.setZipCode(addressDetails.getZipCode());
                                addressService.saveOrUpdate(existingAddress);
                                return ResponseEntity.ok().build();
                            }).orElse(ResponseEntity.badRequest().body("Student not found"));
                }).orElse(ResponseEntity.notFound().build());
    }
}
