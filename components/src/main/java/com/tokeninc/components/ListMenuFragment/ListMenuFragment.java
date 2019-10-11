package com.tokeninc.components.ListMenuFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tokeninc.components.R;

import java.util.List;

;


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
}
