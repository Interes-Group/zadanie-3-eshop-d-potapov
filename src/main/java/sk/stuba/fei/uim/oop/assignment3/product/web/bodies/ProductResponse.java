package sk.stuba.fei.uim.oop.assignment3.product.web.bodies;

import lombok.Getter;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long amount;
    private String unit;
    private float price;
    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.amount = product.getAmount();
        this.unit = product.getUnit();
        this.price = product.getPrice();
    }
}
