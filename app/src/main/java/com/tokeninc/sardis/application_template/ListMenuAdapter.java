package com.tokeninc.sardis.application_template;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.ListItemViewHolder> {
    private static String TAG = "ListMenuAdapter";
    private List<ListMenuItemFormat> mMenuListItems;
    private static ListMenuClickListener mClickListener;

    public void setmClickListener(ListMenuClickListener mClickListener) {
        ListMenuAdapter.mClickListener = mClickListener;
    }



    public ListMenuAdapter(List<ListMenuItemFormat> menuListItems){
        Log.d(TAG,"ListMenuAdapter");
        this.mMenuListItems = menuListItems;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder");
        View textView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_menu_item,
                viewGroup,false);

        ListItemViewHolder listItemViewHolder = new ListItemViewHolder(textView);
        return listItemViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder listItemViewHolder, int i) {
        Log.d(TAG,"onBindViewHolder");
        listItemViewHolder.mListItemView.setTag(mMenuListItems.get(i).tag);
        listItemViewHolder.bind(mMenuListItems.get(i).name);
    }

    @Override
    public int getItemCount() {
        return mMenuListItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mListItemView;
        private int mPosition;


        public int getmPosition() {
            return mPosition;
        }

        public void setmPosition(int mPosition) {
            this.mPosition = mPosition;
        }


        public ListItemViewHolder(View view){
            super(view);
            Log.d(TAG,"ListItemViewHolder");
            mListItemView = view.findViewById(R.id.tv_menu_item_name);
            view.setOnClickListener(this);
        }

        void bind(String text){
            Log.d(TAG,"ListItemViewHolder - Bind");
            mListItemView.setText(text);
        }

        @Override
        public void onClick(View v) {
            String tag = (String) v.findViewById(R.id.tv_menu_item_name).getTag();
            mClickListener.onItemClick(tag,v);
        }
    }
    public interface ListMenuClickListener{
        void onItemClick(String tag,View view);
    }
}
