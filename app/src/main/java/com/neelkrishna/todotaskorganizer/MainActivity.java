package com.neelkrishna.todotaskorganizer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity implements ToDoListFragment.Callbacks, ToDoItemFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new ToDoListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onToDoItemSelected(ToDoItem toDoItem) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = ToDoItemPagerActivity.newIntent(this, toDoItem.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = ToDoItemFragment.newInstance(toDoItem.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onToDoItemUpdated(ToDoItem toDoItem) {
        ToDoListFragment listFragment = (ToDoListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
