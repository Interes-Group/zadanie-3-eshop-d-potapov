package sk.stuba.fei.uim.oop.assignment3.product.logic;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.product.data.IProductRepository;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;

import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository repository;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getById(Long id) throws NotFoundException {
        Product product = repository.findProductById(id);
        if (product == null) {
            throw new NotFoundException("");
        }
        return product;
    }

    @Override
    public Product create(ProductRequest request) {
        return repository.save(new Product(request));
    }

    @Override
    public Product update(Long id, ProductRequest request) throws NotFoundException {
        Product product = getById(id);
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        return repository.save(product);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        repository.delete(getById(id));
    }
}
