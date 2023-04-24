package com.li.gddisease.ui.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.li.gddisease.MainActivity;
import com.li.gddisease.R;

import java.util.Calendar;

import util.ToastUtil;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DateListener listener;
    public interface DateListener{
        void click(String s);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Fragment fragment = ((MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            listener = (DateListener) fragment;
        }catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement click interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.click(year + "/" + month + "/" + dayOfMonth);
    }
}
