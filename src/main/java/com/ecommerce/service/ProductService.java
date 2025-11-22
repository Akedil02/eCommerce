package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "productsAll")
    public List<Product> getAllProducts() {
        System.out.println("Fetching all products from DB...");
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        System.out.println("Fetching product no." + id + " from DB...");
        return productRepository.findById(id);
    }
    //clear cache after creating product
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true)
    })
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    //clear cache after updating
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
    })
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedProduct.getName());
                    existing.setDescription(updatedProduct.getDescription());
                    existing.setPrice(updatedProduct.getPrice());
                    existing.setQuantity(updatedProduct.getQuantity());
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
    })
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
