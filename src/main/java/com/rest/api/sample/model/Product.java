package com.rest.api.sample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
@SequenceGenerator(name = "sq_products", sequenceName = "sq_products", allocationSize = 1)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product implements Comparable<Product> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_products")
    private Long id;
    private String name;
    private String category;
    @JsonProperty("retail_price")
    private Double retailPrice;
    @JsonProperty("discounted_price")
    private Double discountedPrice;
    private Boolean availability;

    public double calculate(){
        double d = ((retailPrice - discountedPrice) / retailPrice) * 100;
        return BigDecimal
                .valueOf(d)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public int compareTo(Product product) {
        if (this.calculate() != product.calculate()) {
            if(this.calculate() < product.calculate()){
                return 1;
            } else
                return -1;
        }

        if(this.getDiscountedPrice() < product.getDiscountedPrice())
            return -1;
        else if(this.getDiscountedPrice() > product.getDiscountedPrice())
            return 1;

        if(this.getId() < product.getId())
            return -1;
        else
            return 1;
    }
}
