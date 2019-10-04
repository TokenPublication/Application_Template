package com.tokeninc.sardis.application_template.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Adapters.ListMenuAdapter;
import com.tokeninc.sardis.application_template.UI.Definitions.IListMenuItem;

import java.util.List;


/**
 * Use the {@link ListMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMenuFragment extends Fragment {
    private List<IListMenuItem> menuItemList;
    public ListMenuClickListener menuClickListener;
    private ListMenuAdapter mListMenuAdapter;

    public ListMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuItemList List of objects of classes those implement {@link IListMenuItem} interface.
     * @param listener Listener object which implements {@link ListMenuClickListener} interface to handle menu item click events.
     * @return A new instance of fragment ListMenuFragment.
     */
    public static ListMenuFragment newInstance(List<IListMenuItem> menuItemList, ListMenuClickListener listener) {
        ListMenuFragment fragment = new ListMenuFragment();
        fragment.setListItems(menuItemList);
        fragment.setListener(listener);
        return fragment;
    }

    public void setListItems(List<IListMenuItem> list) {
        this.menuItemList = list;
        if (mListMenuAdapter != null) {
            mListMenuAdapter.notifyDataSetChanged();
        }
    }

    public void setListener(@Nullable ListMenuClickListener listener) {
        this.menuClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_list_menu, container, false);
        RecyclerView mRecyclerView = mView.findViewById(R.id.rv_list_menu);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setHasFixedSize(true);

        mListMenuAdapter = new ListMenuAdapter(menuItemList, menuClickListener);
        mRecyclerView.setAdapter(mListMenuAdapter);
        return mView;
    }

    /**
     * The click listener interface which has to be implemented by the object which is passed as second parameter to the
     * {@code newInstance(List<IListMenuItem> menuItemList, ListMenuClickListener listener)} factory method.
     */
    public interface ListMenuClickListener{
        /**
         * @param position: Order of tapped list item in the list menu
         * @param item: This is the instance of your list object's generic class which implements {@link IListMenuItem} interface.
         */
        void onItemClick(int position, IListMenuItem item);
    }
}
