package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.sam.team.character.R;

/**
 * Created by pborisenko on 11/5/2016.
 */

public class FragmentEditField extends Fragment {

    private static final String TAG = "FragmentEditField";

    private EditText editTextName;
    private EditText editTextValue;

    private NumberPicker pickerType;

    private RadioButton radioButtonEditableYes;
    private RadioButton radioButtonEditableNo;

    private CheckBox checkBoxName;
    private CheckBox checkBoxType;
    private CheckBox checkBoxEditable;
    private CheckBox checkBoxValue;

    private Button btnOK;
    private Button btnCancel;

    private String name;
    private String type;
    private Boolean editable;
    private String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_field, null);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_field_title);

        editTextName = (EditText) view.findViewById(R.id.stage_name_text);
        editTextValue = (EditText) view.findViewById(R.id.stage_value_text);

        pickerType = (NumberPicker) view.findViewById(R.id.stage_type_picker);

        radioButtonEditableYes = (RadioButton) view.findViewById(R.id.stage_editable_yes);
        radioButtonEditableNo = (RadioButton) view.findViewById(R.id.stage_editable_no);

        checkBoxName = (CheckBox) view.findViewById(R.id.stage_name_check);
        checkBoxType = (CheckBox) view.findViewById(R.id.stage_type_check);
        checkBoxEditable = (CheckBox) view.findViewById(R.id.stage_editable_check);
        checkBoxValue = (CheckBox) view.findViewById(R.id.stage_value_check);

        btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        return view;
    }

    private void moveToNextState() {
        if (name != null) {
            if (pickerType.getVisibility() == View.INVISIBLE) {
                pickerType.setVisibility(View.VISIBLE);
            }
        }
    }
}