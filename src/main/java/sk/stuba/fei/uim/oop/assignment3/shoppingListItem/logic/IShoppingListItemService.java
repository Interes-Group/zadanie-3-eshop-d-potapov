package sk.stuba.fei.uim.oop.assignment3.shoppingListItem.logic;

import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;

public interface IShoppingListItemService {
    void decreaseAmount(ShoppingListItem item) throws IllegalOperationException;
    ShoppingListItem save(ShoppingListItem item);
    ShoppingListItem create();
}
