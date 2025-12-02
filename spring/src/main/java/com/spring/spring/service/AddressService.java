package com.spring.spring.service;

import com.spring.spring.entity.AddressEntity;
import com.spring.spring.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void saveOrUpdate(AddressEntity address) {
        addressRepository.save(address);
    }

    public Optional<AddressEntity> getAddress(Long id) {
        return addressRepository.findById(id);
    }
}
