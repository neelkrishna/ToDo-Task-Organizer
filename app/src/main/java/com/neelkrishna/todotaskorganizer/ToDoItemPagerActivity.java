package com.neelkrishna.todotaskorganizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by neelkrishna
 */
public class ToDoItemPagerActivity extends AppCompatActivity implements ToDoItemFragment.Callbacks{
    private static final String EXTRA_ID = "com.neelkrishna.todotaskorganizer.id";

    private ViewPager mViewPager;
    private List<ToDoItem> mToDoItems;

    public static Intent newIntent(Context packageContext, UUID id) {
        Intent intent = new Intent(packageContext, ToDoItemPagerActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        UUID id = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_pager_view_pager);

        mToDoItems = Singleton.get(this).getToDoItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                ToDoItem toDoItem = mToDoItems.get(position);
                return ToDoItemFragment.newInstance(toDoItem.getId());
            }

            @Override
            public int getCount() {
                return mToDoItems.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ToDoItem toDoItem = mToDoItems.get(position);
                if (toDoItem.getTitle() != null) {
                    setTitle(toDoItem.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        for (int i = 0; i < mToDoItems.size(); i++) {
            if (mToDoItems.get(i).getId().equals(id)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onToDoItemUpdated(ToDoItem toDoItem) {

    }
}
