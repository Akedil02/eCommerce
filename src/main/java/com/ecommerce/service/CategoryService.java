package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // all in "categoriesAll"
    @Cacheable(value = "categoriesAll")
    public List<Category> findAll() {
        System.out.println("Fetching all categories from DB...");
        return categoryRepository.findAll();
    }

    // by id in "category" + id
    @Cacheable(value = "category", key = "#id")
    public Category findById(Long id) {
        System.out.println("Fetching category " + id + " from DB...");
        return categoryRepository.findById(id).orElse(null);
    }

    // add new, clear cache list
    @Caching(evict = {
            @CacheEvict(value = "categoriesAll", allEntries = true)
    })
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // clear category, clean cache list
    @Caching(evict = {
            @CacheEvict(value = "categoriesAll", allEntries = true),
            @CacheEvict(value = "category", key = "#id")
    })
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
