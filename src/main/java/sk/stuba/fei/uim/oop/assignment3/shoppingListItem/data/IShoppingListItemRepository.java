package sk.stuba.fei.uim.oop.assignment3.shoppingListItem.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
}
