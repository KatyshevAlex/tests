package com.rest.api.sample.service.implementations;


import com.rest.api.sample.model.Product;
import com.rest.api.sample.repository.ProductRepo;
import com.rest.api.sample.service.IMainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainService implements IMainService {

    ProductRepo repository;

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        boolean exist = false;
        if(product.getId() != null)
            exist = repository.existsById(product.getId());

        if(exist){
            return null;
        } else {
            return repository.save(product);
        }
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        boolean exist = repository.existsById(id);
        if(exist){
            Product saved = repository.getOne(id);
            if(product.getRetailPrice() != null)
                saved.setRetailPrice(product.getRetailPrice());
            if(product.getDiscountedPrice() != null)
                saved.setDiscountedPrice(product.getDiscountedPrice());
            if(product.getAvailability() != null)
                saved.setAvailability(product.getAvailability());
            return repository.save(saved);
        } else {
            return null;
        }
    }

    @Override
    public Product getProductById(Long id) {
        boolean exist = repository.existsById(id);
        if(exist){
            return repository.getOne(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Product> getList(String category) {
        return repository.findSorted(category);
    }

    @Override
    public List<Product> getListByTwoParams(String category, String availability) {
        List<Product> answer;

        if(availability.equals("1")) {
            answer = repository.findTwoParams(category, true).stream()
                    .sorted(Product::compareTo)
                    .collect(Collectors.toList());
        } else {
            answer = repository.findTwoParams(category, false).stream()
                    .sorted(Product::compareTo)
                    .collect(Collectors.toList());
        }

        return answer;
    }

    @Override
    public List<Product> getFullList() {
        return repository.findAllSortedById();
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}
