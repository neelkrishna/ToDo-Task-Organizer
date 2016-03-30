package com.neelkrishna.todotaskorganizer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by neelkrishna
 */
public class ToDoListFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private ToDoItemAdapter mAdapter;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onToDoItemSelected(ToDoItem toDoItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todoitem_list, container, false);

        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_todoitem_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_todoitem:
                ToDoItem toDoItem = new ToDoItem();
                Singleton.get(getActivity()).addToDoItem(toDoItem);
                updateUI();
                mCallbacks.onToDoItemSelected(toDoItem);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        Singleton singleton = Singleton.get(getActivity());
        List<ToDoItem> toDoItems = singleton.getToDoItems();

        if (mAdapter == null) {
            mAdapter = new ToDoItemAdapter(toDoItems);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setToDoItems(toDoItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ToDoItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mCompletedCheckBox;

        private ToDoItem mToDoItem;

        public ToDoItemHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date_text_view);
            mCompletedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_checkbox);
        }

        public void bindToDoItem(ToDoItem toDoItem) {
            mToDoItem = toDoItem;
            mTitleTextView.setText(mToDoItem.getTitle());
            mDateTextView.setText(mToDoItem.getDueDate().toString());
            mCompletedCheckBox.setChecked(mToDoItem.isCompleted());
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onToDoItemSelected(mToDoItem);
        }
    }

    private class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemHolder> {

        private List<ToDoItem> mToDoItems;

        public ToDoItemAdapter(List<ToDoItem> toDoItems) {
            mToDoItems = toDoItems;
        }

        @Override
        public ToDoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new ToDoItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ToDoItemHolder holder, int position) {
            ToDoItem toDoItem = mToDoItems.get(position);
            holder.bindToDoItem(toDoItem);
        }

        @Override
        public int getItemCount() {
            return mToDoItems.size();
        }

        public void setToDoItems(List<ToDoItem> toDoItems) {
            mToDoItems = toDoItems;
        }
    }

}
