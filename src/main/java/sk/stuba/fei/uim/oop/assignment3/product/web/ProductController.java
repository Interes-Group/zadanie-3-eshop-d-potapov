package sk.stuba.fei.uim.oop.assignment3.product.web;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.logic.IProductService;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponse> getAllAuthors() {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> products = service.getAll();
        for (Product product : products) {
            productResponses.add(new ProductResponse(product));
        }
        return productResponses;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse getAllAuthors(@PathVariable("id") Long productId) throws NotFoundException {
        return new ProductResponse(service.getById(productId));
    }
}
