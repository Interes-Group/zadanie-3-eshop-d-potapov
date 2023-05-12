package sk.stuba.fei.uim.oop.assignment3.product.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
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
            throw new NotFoundException();
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
    public void delete(Long id) throws NotFoundException {
        repository.delete(getById(id));
    }

    @Override
    public Long getAmount(Long id) throws NotFoundException {
        return getById(id).getAmount();
    }

    @Override
    public Long addAmount(Long id, Long increment) throws NotFoundException {
        Product product = getById(id);
        product.setAmount(product.getAmount() + increment);
        repository.save(product);
        return product.getAmount();
    }

    @Override
    public void decreaseAmount(Long id, Long decrement) throws NotFoundException, IllegalOperationException {
        Product product = getById(id);
        if (product.getAmount() - decrement < 0){
            throw new IllegalOperationException();
        }
        product.setAmount(product.getAmount() - decrement);
        repository.save(product);
    }
}
