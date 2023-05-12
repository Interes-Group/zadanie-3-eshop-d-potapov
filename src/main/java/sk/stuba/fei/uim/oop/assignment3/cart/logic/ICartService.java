package sk.stuba.fei.uim.oop.assignment3.cart.logic;

import sk.stuba.fei.uim.oop.assignment3.cart.data.Cart;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.web.bodies.ShoppingListItemRequest;

public interface ICartService {
    Cart create();
    Cart getById(Long id) throws NotFoundException;
    void delete(Long id) throws NotFoundException, IllegalOperationException;
    Cart addToCart(Long id, ShoppingListItemRequest body) throws NotFoundException, IllegalOperationException;
    String payCart(Long id) throws NotFoundException, IllegalOperationException;
}
