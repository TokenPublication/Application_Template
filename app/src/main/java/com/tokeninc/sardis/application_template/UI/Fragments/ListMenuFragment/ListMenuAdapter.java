package com.tokeninc.sardis.application_template.UI.Fragments.ListMenuFragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.IListMenuItem;

import java.util.List;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.ListItemViewHolder> {
    /**
     * mMenuListItems is the list of objects(any type) those implement {@link IListMenuItem} interface.
     * Implement this interface in your class, then implement getName() method and return the property which you want to see in the list item.
     */
    private List<IListMenuItem> mMenuListItems;
    private ListMenuClickListener mClickListener;

    public ListMenuAdapter(List<IListMenuItem> menuListItems, ListMenuClickListener mClickListener){
        this.mMenuListItems = menuListItems;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item_row,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder listItemViewHolder, int i) {
        listItemViewHolder.mListItemView.setTag(mMenuListItems.get(i).getName());
        listItemViewHolder.bind(mMenuListItems.get(i).getName(), i);

    }

    @Override
    public int getItemCount() {
        return mMenuListItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mListItemView;
        private int position;

        ListItemViewHolder(View view){
            super(view);
            mListItemView = view.findViewById(R.id.menu_item_title);
            view.setOnClickListener(this);
        }

        void bind(String text, int position){
            mListItemView.setText(text);
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(position, mMenuListItems.get(position));
        }
    }

}
