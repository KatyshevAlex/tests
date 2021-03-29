package com.rest.api.sample.controller;

import com.rest.api.sample.model.Product;
import com.rest.api.sample.service.IMainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    IMainService mainService;

    @InjectMocks
    ProductsController controller;

    private Product p;

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
    @Order(1)
    void addProduct() {
        // given
        given(mainService.saveProduct(
                argThat(product -> {
                    assertEquals(p.getId(), product.getId());
                    assertEquals(p.getName(), product.getName());
                    assertEquals(p.getCategory(), product.getCategory());
                    assertTrue(product.getAvailability());
                    assertEquals(p.getRetailPrice(), product.getRetailPrice());
                    assertEquals(p.getDiscountedPrice(), product.getDiscountedPrice());
                    return true;
                }))).willReturn(p);

        // when
        Product result  = controller.addProduct(p).getBody();

        // then
        then(mainService)
                .should(times(1))
                .saveProduct(p);
        assertNotNull(result);
        assertSame(p.getId(), result.getId());
        assertEquals(p.getName(), result.getName());
        assertEquals(p.getCategory(), result.getCategory());
        assertTrue(result.getAvailability());
        assertSame(p.getRetailPrice(), result.getRetailPrice());
        assertSame(p.getDiscountedPrice(), result.getDiscountedPrice());
    }

    @Test
    @Order(2)
    void updateProduct() {
        // given
        p.setName("NewName");
        given(mainService.updateProduct(eq(0L), argThat(pr -> {
                assertEquals(p.getName(), pr.getName());
                return true;
        }))).willReturn(p);

        // when
        Product result = controller.updateProduct(0L, p).getBody();

        // then
        then(mainService)
                .should(times(1))
                .updateProduct(0L, p);
        assertNotNull(result);
        assertEquals(p.getName(), result.getName());
    }

    @Test
    @Order(3)
    void getProduct() {
        // given
        given(mainService.getProductById(eq(0L))).willReturn(p);

        // when
        Product product = controller.getProduct(0L).getBody();

        // then
        then(mainService)
                .should(times(1))
                .getProductById(0L);

        assertNotNull(product);
        assertEquals(p.getName(), product.getName());
    }

    @Test
    void getList() throws UnsupportedEncodingException {
        // given
        List<Product> list = Arrays.asList(p,p,p);
        given(mainService.getFullList()).willReturn(list);

        // when
        List<Product> result = controller.getList(null, null).getBody();

        // then
        then(mainService)
                .should(times(1))
                .getFullList();

        assertNotNull(result);
        assertSame(list.size(), result.size());
        assertEquals(list.get(0).getName(), result.get(0).getName());
    }
}