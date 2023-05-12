package sk.stuba.fei.uim.oop.assignment3.cart.web.bodies;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.cart.data.Cart;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.web.bodies.ShoppingListItemResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponse {
   private Long id;
   private List<ShoppingListItemResponse> shoppingList;
   private boolean payed;
   public CartResponse(Cart cart){
      this.id = cart.getId();
      this.payed = cart.isPayed();
      this.shoppingList = new ArrayList<>();
      for (ShoppingListItem item : cart.getShoppingList()){
         this.shoppingList.add(new ShoppingListItemResponse(item));
      }
   }
}
