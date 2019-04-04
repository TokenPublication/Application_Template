package com.tokeninc.sardis.application_template;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class EditLineListAdapter extends RecyclerView.Adapter<EditLineListAdapter.EditLineViewHolder> {
    private static String TAG = "EditLineListAdapter";
    private List<Pair<String,String>> tagHintPairs;
    private TextWatcherInterface textWatcherInterface;

    public EditLineListAdapter(List<Pair<String,String>> tagHintPairs){
        this.tagHintPairs = tagHintPairs;
    }

    public void setTextWatcherInterface(TextWatcherInterface textWatcherInterface) {
        this.textWatcherInterface = textWatcherInterface;
    }

    @Override
    public int getItemCount() {
        return tagHintPairs.size();
    }

    @NonNull
    @Override
    public EditLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_line_list_item,
                viewGroup,false);

        EditLineViewHolder editLineViewHolder = new EditLineViewHolder(view);
        return editLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EditLineViewHolder editLineViewHolder, int i) {
        Log.d(TAG,"onBindViewHolder");

        editLineViewHolder.editText.setTag(i);
        editLineViewHolder.bind(tagHintPairs.get(i).first,tagHintPairs.get(i).second);
    }

    public class EditLineViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        EditText editText;

        public EditLineViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.tv_form_title);
            editText = view.findViewById(R.id.et_form_hint);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    textWatcherInterface.beforeTextChanged(s,start,count,after,(int)editText.getTag());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textWatcherInterface.onTextChanged(s,start,before,count,(int)editText.getTag());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    textWatcherInterface.afterTextChanged(s,(int)editText.getTag());
                }
            });
        }

        public void bind(String editLineTag, String editLineHint){
            textView.setText(editLineTag);
            editText.setText(editLineHint);
        }
    }

    public interface TextWatcherInterface{
        void beforeTextChanged(CharSequence s, int start, int count, int after, int tag);
        void onTextChanged(CharSequence s, int start, int before, int count,int tag);
        void afterTextChanged(Editable s, int tag);
    }
}
