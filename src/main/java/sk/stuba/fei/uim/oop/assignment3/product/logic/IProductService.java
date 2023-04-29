package sk.stuba.fei.uim.oop.assignment3.product.logic;

import javassist.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;

import java.util.List;

public interface IProductService {
    List<Product> getAll();
    Product getById(long id) throws NotFoundException;
    Product create(ProductRequest request);

}
