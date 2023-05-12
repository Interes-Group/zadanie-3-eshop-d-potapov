package sk.stuba.fei.uim.oop.assignment3.shoppingListItem.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.IShoppingListItemRepository;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;

@Service
public class ShoppingListItemService implements IShoppingListItemService {
    @Autowired
    private IShoppingListItemRepository repository;

    @Override
    public void decreaseAmount(ShoppingListItem item) throws IllegalOperationException {
        if (item.getAmount() < 1) {
            throw new IllegalOperationException();
        }
        item.setAmount(item.getAmount() - 1);
        repository.save(item);
    }

    @Override
    public ShoppingListItem save(ShoppingListItem item) {
        return this.repository.save(item);
    }

    @Override
    public ShoppingListItem create() {
        return this.repository.save(new ShoppingListItem());
    }

}
