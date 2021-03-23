package com.rest.api.sample.service;



import com.rest.api.sample.model.Product;

import java.util.List;

public interface IMainService {

    Product saveProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product getProductById(Long id);

    List<Product> getList(String category);

    List<Product> getListByTwoParams(String category, String availability);

    List<Product> getFullList();

    void deleteProduct(Long id);
}
