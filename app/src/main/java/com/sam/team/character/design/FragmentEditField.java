package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sam.team.character.R;
import com.sam.team.character.core.Field;
import com.sam.team.character.viewmodel.CleanOnTouchListener;

/**
 * Created by pborisenko on 11/5/2016.
 */

public class FragmentEditField extends Fragment {

    private static final String TAG = "FragmentEditField";

    private LinearLayout nameContainer;
    private LinearLayout typeContainer;
    private LinearLayout editableContainer;
    private LinearLayout valueContainer;

    private EditText editTextName;
    private EditText editTextValue;

    private NumberPicker pickerType;

    private RadioGroup  radioGroupEditable;
    private RadioButton radioButtonEditableYes;
    private RadioButton radioButtonEditableNo;

    private CheckBox checkBoxName;
    private CheckBox checkBoxType;
    private CheckBox checkBoxEditable;
    private CheckBox checkBoxValue;

    private Button btnOK;
    private Button btnCancel;

    private Field.FieldType type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_field, null);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_field_title);


        /*----------------------------------- NAME STEP -----------------------------------*/
        nameContainer = (LinearLayout) view.findViewById(R.id.stage_name);
        checkBoxName = (CheckBox) nameContainer.findViewById(R.id.stage_name_check);
        editTextName = (EditText) nameContainer.findViewById(R.id.stage_name_text);
        final Step nameStep = new Step(checkBoxName, nameContainer);

        editTextName.setOnTouchListener(new CleanOnTouchListener(getActivity(), editTextName,
                R.string.edit_field_dflt_name));
        editTextName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String str = editTextName.getText().toString();
                if (CleanOnTouchListener.isValidString(FragmentEditField.this.getActivity(), str,
                        R.string.edit_field_dflt_name)) {
                    nameStep.setValid(true);
                } else {
                    nameStep.setValid(false);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /*----------------------------------- TYPE STEP -----------------------------------*/
        typeContainer = (LinearLayout) view.findViewById(R.id.stage_type);
        checkBoxType = (CheckBox) typeContainer.findViewById(R.id.stage_type_check);
        pickerType = (NumberPicker) typeContainer.findViewById(R.id.stage_type_picker);
        final Step typeStep = new Step(checkBoxType, typeContainer);

        String[] stringTypes = new String[] {
                getActivity().getResources().getString(R.string.edit_field_dflt_type),
                Field.FieldType.SHORT_TEXT.name(),
                Field.FieldType.LONG_TEXT.name(),
                Field.FieldType.NUMERIC.name(),
                Field.FieldType.CALCULATED.name()
        };
        pickerType.setMinValue(0);
        pickerType.setMinValue(4);
        pickerType.setDisplayedValues(stringTypes);

        pickerType.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal != 0) {
                    typeStep.setValid(true);
                } else {
                    typeStep.setValid(false);
                }
            }
        });

        /*----------------------------------- VALUE STEP -----------------------------------*/
        valueContainer = (LinearLayout) view.findViewById(R.id.stage_value);
        checkBoxValue = (CheckBox) valueContainer.findViewById(R.id.stage_value_check);
        editTextValue = (EditText) valueContainer.findViewById(R.id.stage_value_text);
        final Step valueStep = new Step(checkBoxValue, valueContainer);

        editTextValue.setOnTouchListener(new CleanOnTouchListener(getActivity(), editTextValue,
                R.string.edit_field_dflt_value));
        editTextValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String str = editTextValue.getText().toString();
                if (CleanOnTouchListener.isValidString(FragmentEditField.this.getActivity(), str,
                        R.string.edit_field_dflt_value)) {
                    valueStep.setValid(true);
                } else {
                    valueStep.setValid(false);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /*-------------------------------- EDITABLE STEP -----------------------------------*/
        editableContainer = (LinearLayout) view.findViewById(R.id.stage_editable);
        checkBoxEditable = (CheckBox) editableContainer.findViewById(R.id.stage_editable_check);
        radioGroupEditable = (RadioGroup) editableContainer.findViewById(R.id.stage_editable_group);
        radioButtonEditableYes = (RadioButton) editableContainer.findViewById(R.id.stage_editable_yes);
        radioButtonEditableNo = (RadioButton) editableContainer.findViewById(R.id.stage_editable_no);
        final Step editableStep = new Step(checkBoxEditable, editableContainer) {
            @Override
            void enable() {
                if (type != Field.FieldType.CALCULATED) {
                    super.enable();
                } else {
                    radioButtonEditableNo.setChecked(true);
                    valueStep.enable();
                }
            }
        };

        radioGroupEditable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editableStep.setValid(true);
            }
        });

        /*---------------------------------- BUTTONS --------------------------------------*/

        btnOK = (Button) view.findViewById(R.id.btn_ok);

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEditField.this.getActivity().onBackPressed();
            }
        });

        /*--------------------------------- RELATIONS -------------------------------------*/
        nameStep.addControlChild(typeStep);
        typeStep.addControlChild(editableStep);
        typeStep.addControlChild(valueStep);
        editableStep.addControlChild(valueStep);

        return view;
    }
}