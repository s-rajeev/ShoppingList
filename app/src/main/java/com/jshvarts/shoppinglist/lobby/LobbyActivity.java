package com.jshvarts.shoppinglist.lobby;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.jshvarts.shoppinglist.R;
import com.jshvarts.shoppinglist.lobby.fragments.AddShoppingListItemFragment;
import com.jshvarts.shoppinglist.lobby.fragments.ViewShoppingListFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LobbyActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(v -> attachAddShoppingListItemFragment());

        attachViewShoppingListFragment();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private void attachViewShoppingListFragment() {
        if (fragmentContainer.getChildCount() == 0) {
            Fragment shoppingListFragment = new ViewShoppingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, shoppingListFragment)
                    .commit();
        }
    }

    private void attachAddShoppingListItemFragment() {
        Fragment addShoppingListItemFragment = new AddShoppingListItemFragment();
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .replace(R.id.fragment_container, addShoppingListItemFragment)
                .commit();
    }
}
