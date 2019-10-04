package com.tokeninc.sardis.application_template.UI.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder> {

    private List<MenuItem> mMenuItems;
    private final OnItemClickListener listener;

    public MenuItemAdapter(List<MenuItem> mMenuItems, OnItemClickListener listener) {
        this.mMenuItems = mMenuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_item_row, viewGroup, false);

        return new MenuItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder menuItemHolder, int i) {
        MenuItem item = mMenuItems.get(i);
        menuItemHolder.mTitle.setText(item.getTitle());
        menuItemHolder.bind(item, i,listener);
        if( item.getIconId() != 0)
            menuItemHolder.mIcon.setImageResource(item.getIconId());
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder{
        public ImageView mIcon;
        public TextView mTitle;
        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.menu_item_title);
        }

        public void bind(final MenuItem item, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item, int position);
    }
}
