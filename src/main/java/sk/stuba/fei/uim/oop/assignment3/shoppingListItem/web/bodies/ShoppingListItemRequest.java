package sk.stuba.fei.uim.oop.assignment3.shoppingListItem.web.bodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListItemRequest {
    private Long productId;
    private Long amount;
}
