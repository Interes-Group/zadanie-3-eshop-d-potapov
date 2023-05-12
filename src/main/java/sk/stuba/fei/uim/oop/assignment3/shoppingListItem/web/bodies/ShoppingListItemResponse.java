package sk.stuba.fei.uim.oop.assignment3.shoppingListItem.web.bodies;

import lombok.Getter;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;

@Getter
public class ShoppingListItemResponse {
    private final Long productId;
    private final Long amount;
    public ShoppingListItemResponse(ShoppingListItem item){
        this.productId = item.getProduct().getId();
        this.amount = item.getAmount();
    }
}
