package sk.stuba.fei.uim.oop.assignment3.cart.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.cart.data.Cart;
import sk.stuba.fei.uim.oop.assignment3.cart.data.ICartRepository;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.product.data.Product;
import sk.stuba.fei.uim.oop.assignment3.product.logic.IProductService;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.logic.IShoppingListItemService;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.web.bodies.ShoppingListItemRequest;

import java.util.List;

@Service
public class CartService implements ICartService {
    @Autowired
    private ICartRepository repository;
    @Autowired
    private IShoppingListItemService itemService;
    @Autowired
    private IProductService productService;

    @Override
    public Cart create() {
        return repository.save(new Cart());
    }

    @Override
    public Cart getById(Long id) throws NotFoundException {
        Cart cart = repository.findCartById(id);
        if (cart == null) {
            throw new NotFoundException();
        }
        return cart;
    }

    @Override
    public void delete(Long id) throws NotFoundException, IllegalOperationException {
        Cart cart = getById(id);
        if (cart.isPayed()) {
            for (ShoppingListItem item : cart.getShoppingList()) {
                itemService.decreaseAmount(item);
            }
        }
        repository.delete(cart);
    }

    @Override
    public Cart addToCart(Long id, ShoppingListItemRequest body) throws NotFoundException, IllegalOperationException {
        Cart cart = getById(id);
        if (cart.isPayed()) {
            throw new IllegalOperationException();
        }

        List<ShoppingListItem> shoppingList = cart.getShoppingList();
        Product product = productService.getById(body.getProductId());
        productService.decreaseAmount(body.getProductId(), body.getAmount());
        for (ShoppingListItem item : shoppingList) {
            if (item.getProduct().equals(product)) {
                item.setAmount(item.getAmount() + body.getAmount());
                itemService.save(item);
                return repository.save(cart);
            }
        }

        ShoppingListItem newItem = itemService.create();
        newItem.setProduct(product);
        newItem.setAmount(body.getAmount());
        cart.getShoppingList().add(itemService.save(newItem));
        return repository.save(cart);
    }

    @Override
    public String payCart(Long id) throws NotFoundException, IllegalOperationException {
        Cart cart = getById(id);
        if (cart.isPayed()) {
            throw new IllegalOperationException();
        }

        double price = 0;
        for (ShoppingListItem item : cart.getShoppingList()){
            System.out.println(item.getAmount());
            System.out.println(item.getProduct().getPrice());
            price += item.getAmount() * item.getProduct().getPrice();
        }
        cart.setPayed(true);
        repository.save(cart);
        System.out.println(price);
        return Double.toString(price);
    }
}
