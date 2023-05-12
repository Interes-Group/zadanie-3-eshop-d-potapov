package sk.stuba.fei.uim.oop.assignment3.product.logic;

import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;

import java.util.List;

public interface IProductService {
    List<Product> getAll();
    Product getById(Long id) throws NotFoundException;
    Product create(ProductRequest request);
    Product update(Long id, ProductRequest request) throws NotFoundException;
    void delete(Long id) throws NotFoundException;
    Long getAmount(Long id) throws NotFoundException;
    Long addAmount(Long id, Long increment) throws NotFoundException;
    void decreaseAmount(Long id, Long decrement) throws NotFoundException, IllegalOperationException;
}
