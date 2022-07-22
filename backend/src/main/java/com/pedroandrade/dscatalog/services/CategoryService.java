package com.pedroandrade.dscatalog.services;

import com.pedroandrade.dscatalog.dto.CategoryDTO;
import com.pedroandrade.dscatalog.entities.Category;
import com.pedroandrade.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList());
    }
}
