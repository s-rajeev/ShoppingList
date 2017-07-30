package com.jshvarts.shoppinglist.lobby;

import com.jshvarts.shoppinglist.common.domain.model.ShoppingList;
import com.jshvarts.shoppinglist.common.domain.model.firebase.FirebaseShoppingListRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class CreateShoppingListUseCase {
    private final FirebaseShoppingListRepository repository;

    @Inject
    CreateShoppingListUseCase(FirebaseShoppingListRepository repository) {
        this.repository = repository;
    }

    Observable<ShoppingList> execute() {
        ShoppingList shoppingList = new ShoppingList();
        return repository.add(shoppingList);
    }
}