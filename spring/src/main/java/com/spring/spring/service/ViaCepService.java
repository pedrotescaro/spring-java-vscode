package com.spring.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.spring.dto.CepResponse;

@Service
public class ViaCepService {
    private final RestTemplate restTemplate;

    public ViaCepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CepResponse getAddressByCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("O CEP informado é nulo ou vazio.");
        }
        final String url = "https://viacep.com.br/ws/" + cep.trim() + "/json/";
        
        try {
            CepResponse response = restTemplate.getForObject(url, CepResponse.class);

            if (response == null) {
                throw new RuntimeException("A resposta da API ViaCEP veio nula para o CEP: " + cep);
            }

            return response;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + cep, e);
        }
    }
}
