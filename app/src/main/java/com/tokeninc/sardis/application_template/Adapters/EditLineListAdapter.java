package com.tokeninc.sardis.application_template.Adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tokeninc.sardis.application_template.Definitions.EditTextFormat;
import com.tokeninc.sardis.application_template.R;

import java.util.List;
import java.util.Map;

public class EditLineListAdapter extends RecyclerView.Adapter<EditLineListAdapter.EditLineViewHolder> {
    private static String TAG = "EditLineListAdapter";
    private List<EditTextFormat> mEditTextFormats;
    private TextWatcherInterface textWatcherInterface;
    private Map<String,Integer> inputTypes;
    private final Context mContext;

    public EditLineListAdapter(List<EditTextFormat> mEditTextFormats, Context context){
        this.mEditTextFormats = mEditTextFormats;
        this.mContext = context;
    }

    public void setTextWatcherInterface(TextWatcherInterface textWatcherInterface) {
        this.textWatcherInterface = textWatcherInterface;
    }

    @Override
    public int getItemCount() {
        return mEditTextFormats.size();
    }

    @NonNull
    @Override
    public EditLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_line_list_item,
                viewGroup,false);

        return new EditLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditLineViewHolder editLineViewHolder, int i) {
        Log.d(TAG,"onBindViewHolder");

        editLineViewHolder.editText.setTag(mEditTextFormats.get(i).tag);
        editLineViewHolder.setInputType(mEditTextFormats.get(i).type);
        editLineViewHolder.bind(mEditTextFormats.get(i).tag,mEditTextFormats.get(i).hint);
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
                    textWatcherInterface.beforeTextChanged(s,start,count,after,(String)editText.getTag());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textWatcherInterface.onTextChanged(s,start,before,count,(String)editText.getTag());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    textWatcherInterface.afterTextChanged(s,(String)editText.getTag());
                }
            });
        }

        public void bind(String editLineTag, String editLineHint){
            textView.setText(editLineTag);
            editText.setHint(editLineHint);
        }


        /**
         * Sets edit text format
         * @param inputType Set -2 for Date Picker, -1 for Time Picker. See Android Developer's Manual for other input types.
         */
        public void setInputType(int inputType){
            //editText.setInputType(inputType);
            if(inputType == -2){
                editText.setFocusable(false);
                editText.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                        DatePickerDialog dialog = new DatePickerDialog(mContext,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(dayOfMonth).
                                        append("/").append(month+1).
                                        append("/").append(year);
                                editText.setText(stringBuilder.toString());
                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                        dialog.show();
                    }
                });
            }else if(inputType == -1){
                editText.setFocusable(false);
                editText.setOnClickListener(new View.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(hourOfDay).append(":").append(minute);

                                editText.setText(stringBuilder.toString());
                            }
                        },
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
                        timePickerDialog.show();
                    }
                });
            }else{
                editText.setInputType(inputType);
            }

        }
    }

    public interface TextWatcherInterface{
        void beforeTextChanged(CharSequence s, int start, int count, int after, String tag);
        void onTextChanged(CharSequence s, int start, int before, int count,String tag);
        void afterTextChanged(Editable s, String tag);
    }

}


