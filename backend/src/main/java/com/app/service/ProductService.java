package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.dto.ProductOutputDTO;
import com.app.model.Product;
import com.app.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductOutputDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ProductOutputDTO toDTO(Product product) {
        ProductOutputDTO dto = new ProductOutputDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setUnitPriceCents(product.getUnitPriceCents());
        dto.setActive(product.getActive());
        return dto;
    }
}
