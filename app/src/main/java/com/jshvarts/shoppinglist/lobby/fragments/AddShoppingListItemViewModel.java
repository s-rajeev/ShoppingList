package com.jshvarts.shoppinglist.lobby.fragments;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jshvarts.shoppinglist.common.domain.model.DatabaseConstants;
import com.jshvarts.shoppinglist.common.domain.model.ShoppingList;
import com.jshvarts.shoppinglist.rx.SchedulersFacade;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class AddShoppingListItemViewModel extends ViewModel {

    private final LoadShoppingListUseCase loadShoppingListUseCase;

    private final AddShoppingListItemUseCase addShoppingListItemUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Boolean> shoppingListItemAdded = new MutableLiveData<>();

    AddShoppingListItemViewModel(AddShoppingListItemUseCase addShoppingListItemUseCase,
                                 LoadShoppingListUseCase loadShoppingListUseCase,
                                 SchedulersFacade schedulersFacade) {
        this.addShoppingListItemUseCase = addShoppingListItemUseCase;
        this.loadShoppingListUseCase = loadShoppingListUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    void addShoppingListItem(String shoppingListItemName) {
        loadShoppingListAndAddItem(shoppingListItemName);
    }

    private void loadShoppingListAndAddItem(String shoppingListItemName) {
        disposables.add(loadShoppingListUseCase.loadShoppingList(DatabaseConstants.DEFAULT_SHOPPING_LIST_ID)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .firstElement()
                .subscribe(shoppingList -> addShoppingListItem(shoppingList, shoppingListItemName),
                        throwable -> Timber.e(throwable))
        );
    }

    private void addShoppingListItem(ShoppingList shoppingList, String shoppingListItemName) {
        disposables.add(addShoppingListItemUseCase.execute(shoppingList, shoppingListItemName)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(updatedShoppingList -> {
                            Timber.d("updated shopping list with id " + updatedShoppingList.getId());
                            shoppingListItemAdded.setValue(true);
                        }, throwable -> Timber.e(throwable)
                )
        );
    }

    MutableLiveData<Boolean> isItemAdded() {
        return shoppingListItemAdded;
    }
}
