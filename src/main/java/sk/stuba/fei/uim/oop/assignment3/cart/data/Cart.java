package sk.stuba.fei.uim.oop.assignment3.cart.data;

import lombok.Data;
import sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data.ShoppingListItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(orphanRemoval = true)
    private List<ShoppingListItem> shoppingList;
    private boolean payed;
    public Cart() {
        shoppingList = new ArrayList<>();
    }
}
