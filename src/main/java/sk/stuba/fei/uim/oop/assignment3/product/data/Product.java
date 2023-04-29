package sk.stuba.fei.uim.oop.assignment3.product.data;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Long amount;
    private String unit;
    private double price;

    public Product() {

    }

    public Product(ProductRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.amount = request.getAmount();
        this.unit = request.getUnit();
        this.price = request.getPrice();
    }
}
