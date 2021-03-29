package com.rest.api.sample.service.implementations;

import com.rest.api.sample.model.Product;
import com.rest.api.sample.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock
    ProductRepo repo;

    @InjectMocks
    MainService mainService;

    Product p;

    @BeforeEach
    void setUp(){
        p = new Product();
        p.setId(0L);
        p.setName("name");
        p.setCategory("category");
        p.setRetailPrice(10.5);
        p.setDiscountedPrice(8.5);
        p.setAvailability(true);
    }


    @Test
    void saveProduct() {
        // given
        given(repo.save(argThat(pr ->{
            assertNotNull(pr);
            assertEquals(p.getName(), pr.getName());
            assertSame(p.getId(), pr.getId());
            return true;
        }))).willReturn(p);

        // when
        Product result = mainService.saveProduct(p);

        // then
        then(repo)
                .should(times(1))
                .save(p);

        assertSame(p.getId(), result.getId());
    }
}