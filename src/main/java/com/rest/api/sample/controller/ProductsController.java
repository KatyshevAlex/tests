package com.rest.api.sample.controller;


import com.rest.api.sample.model.Product;
import com.rest.api.sample.service.IMainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@AllArgsConstructor
public class ProductsController {

    IMainService service;

    @PostMapping()
    @ResponseBody
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product response = service.saveProduct(product);
        if(response == null)
            return  new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        Product response = service.updateProduct(id, product);
        if(response == null)
            return  new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product response = service.getProductById(id);
        if(response == null)
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getList(@RequestParam(required = false)  String category,
                                                 @RequestParam(required = false) String availability) throws UnsupportedEncodingException {
        List<Product> response;
        if(availability != null && !availability.isEmpty())
            response = service.getListByTwoParams(URLDecoder.decode(category, "UTF-8"), URLDecoder.decode(availability, "UTF-8"));
        else if(category != null && !category.isEmpty())
            response = service.getList(URLDecoder.decode(category, "UTF-8"));
        else
            response = service.getFullList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteOne(@PathVariable("id") Long id){
        service.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
