package sk.stuba.fei.uim.oop.assignment3.product.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.logic.IProductService;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.AmountRequest;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.AmountResponse;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductRequest;
import sk.stuba.fei.uim.oop.assignment3.product.web.bodies.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> products = service.getAll();
        for (Product product : products) {
            productResponses.add(new ProductResponse(product));
        }
        return productResponses;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest body) {
        Product product = service.create(body);
        ProductResponse response = new ProductResponse(product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse getAllProducts(@PathVariable("id") Long productId) throws NotFoundException {
        return new ProductResponse(service.getById(productId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse updateProduct(@PathVariable("id") Long productId, @RequestBody ProductRequest body) throws NotFoundException {
        return new ProductResponse(service.update(productId, body));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProduct(@PathVariable("id") Long productId) throws NotFoundException {
        service.delete(productId);
    }

    @GetMapping(value = "/{id}/amount", produces = MediaType.APPLICATION_JSON_VALUE)
    public AmountResponse getAmount(@PathVariable("id") Long productId) throws NotFoundException {
        return new AmountResponse(service.getAmount(productId));
    }

    @PostMapping(value = "/{id}/amount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AmountResponse addAmount(@PathVariable("id") Long productId, @RequestBody AmountRequest body) throws NotFoundException {
        return new AmountResponse(service.addAmount(productId, body.getAmount()));
    }
}
