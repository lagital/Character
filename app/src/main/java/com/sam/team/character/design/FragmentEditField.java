package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.sam.team.character.R;
import com.sam.team.character.core.Field;
import com.sam.team.character.viewmodel.CleanOnTouchListener;
import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysField;

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

    private Switch editableSwitch;

    private Button btnOK;
    private Button btnCancel;

    private Integer currentTypeInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_field, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_field_title);

        Step.setTransitionTime((int) getResources().getInteger(R.integer.step_transition_time));

        /*----------------------------------- NAME STEP -----------------------------------*/
        nameContainer = (LinearLayout) view.findViewById(R.id.stage_name);
        editTextName = (EditText) nameContainer.findViewById(R.id.stage_name_text);
        final Step nameStep = new Step(nameContainer) {
            @Override
            void disable() {
                super.disable();
                editTextName.setText(getResources().getString(R.string.edit_field_dflt_name));
            }
        };

        editTextName.setOnTouchListener(new CleanOnTouchListener(getActivity(), editTextName,
                getActivity().getResources().getString(R.string.edit_field_dflt_name)));
        editTextName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String str = editTextName.getText().toString();
                Log.d(TAG, "afterTextChanged to " + str);

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

        /*-------------------------------- EDITABLE STEP -----------------------------------*/
        editableContainer = (LinearLayout) view.findViewById(R.id.stage_editable);
        editableSwitch    = (Switch) view.findViewById(R.id.stage_editable_switch);
        final Step editableStep = new Step(editableContainer) {
            @Override
            void enable() {
                /* calculated fields are always uneditable */
                if (currentTypeInt == 4) {
                    setValid(true);
                    editableSwitch.setChecked(false);
                    editableContainer.setVisibility(View.GONE);
                } else {
                    super.enable();
                }
            }
        };

        editableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Check changed");
                editableStep.setValid(true);
            }
        });
        /*----------------------------------- TYPE STEP -----------------------------------*/
        typeContainer = (LinearLayout) view.findViewById(R.id.stage_type);
        pickerType = (NumberPicker) typeContainer.findViewById(R.id.stage_type_picker);
        final Step typeStep = new Step(typeContainer) {
            @Override
            void disable() {
                super.disable();
                pickerType.setValue(0);
            }
        };

        String[] stringTypes = new String[] {
                getActivity().getResources().getString(R.string.edit_field_dflt_type),
                SysField.formatTypeToName(getActivity(), Field.FieldType.SHORT_TEXT),
                SysField.formatTypeToName(getActivity(), Field.FieldType.LONG_TEXT),
                SysField.formatTypeToName(getActivity(), Field.FieldType.NUMERIC),
                SysField.formatTypeToName(getActivity(), Field.FieldType.CALCULATED)
        };
        pickerType.setMinValue(0);
        pickerType.setMaxValue(stringTypes.length - 1);
        pickerType.setWrapSelectorWheel(false);
        pickerType.setDisplayedValues(stringTypes);

        pickerType.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "Picker - setOnValueChangedListener " + Integer.toString(newVal));
                currentTypeInt = newVal;
                if (newVal != 0) {
                    typeStep.setValid(true);
                } else {
                    typeStep.setValid(false);
                }
            }
        });

        /*----------------------------------- VALUE STEP -----------------------------------*/
        valueContainer = (LinearLayout) view.findViewById(R.id.stage_value);
        editTextValue = (EditText) valueContainer.findViewById(R.id.stage_value_text);
        final Step valueStep = new Step(valueContainer) {
            @Override
            void disable() {
                super.disable();
                editTextValue.setText(R.string.edit_field_dflt_value);
            }
        };

        editTextValue.setOnTouchListener(new CleanOnTouchListener(getActivity(), editTextValue,
                getActivity().getResources().getString(R.string.edit_field_dflt_value)));
        editTextValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String str = editTextValue.getText().toString();
                Boolean stringValid = true;
                int dotCounter = 0;
                Log.d(TAG, "Value - afterTextChanged on " + str);

                if (CleanOnTouchListener.isValidString(FragmentEditField.this.getActivity(), str,
                        R.string.edit_field_dflt_value)) {
                    valueStep.setValid(true);
                } else {
                    valueStep.setValid(false);
                }

                // select element name
                if (str.endsWith("@")) {
                    generateElementMenu(editTextValue).show();
                }

                // select category name
                if (str.endsWith(".")) {
                    // all between last @ and current dot
                    str = str.substring(str.lastIndexOf("@") + 1, str.length() - 1);
                    Log.d(TAG, "String to analyze: " + str);

                    if (str.contains("@") ||
                            str.contains("(") ||
                            str.contains(")") ||
                            str.contains("+") ||
                            str.contains("-") ||
                            str.contains("*") ||
                            str.contains("/")) {
                        stringValid = false;
                        Log.d(TAG, "String validation failed.");
                    };

                    int num = str.length() - str.replace(".", "").length();
                    if (stringValid) {
                        // no previous dots - select category
                        if (num == 0) {
                            generateCategoryMenu(editTextValue).show();
                        }
                        // one previous dot - select field
                        if (num == 1) {
                            generateFieldMenu(editTextValue).show();
                        }
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /*---------------------------------- BUTTONS --------------------------------------*/

        btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnOK onClick");
                Session.getInstance().getElementFromCache().addField(
                        settingsIntoField());
                getFragmentManager().popBackStack();
            }
        });

        final Step btnOKStep = new Step(btnOK) {
            @Override
            void enable() {
                btnOK.setVisibility(View.VISIBLE);
            }

            @Override
            void disable() {
                btnOK.setVisibility(View.INVISIBLE);
            }
        };

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnCancel onClick");
                FragmentEditField.this.getActivity().onBackPressed();
            }
        });

        /*--------------------------------- RELATIONS -------------------------------------*/
        nameStep.addControlChild(typeStep);
        typeStep.addControlChild(editableStep);
        editableStep.addControlChild(valueStep);
        valueStep.addControlChild(btnOKStep);

        return view;
    }

    private SysField settingsIntoField () {
        Log.d(TAG, "settingsIntoField");
        SysField sf = new SysField(
                Session.getInstance().getCategoryFromCache(),
                editTextName.getText().toString(),
                Field.getTypeFromInt(currentTypeInt),
                Session.getInstance().getElementFromCache());
        // TODO: change after core refactoring
        sf.addValue(editTextValue.getText().toString());

        return sf;
    }

    private PopupMenu generateElementMenu (final EditText textAnchor) {
        PopupMenu pm = new PopupMenu(getActivity(), textAnchor);
        Log.d(TAG, "generateElementMenu");

        for (SysElement e : Session.getInstance().getCurrentSystem().getElements()) {
            pm.getMenu().add(e.getName());
        }

        pm.setOnMenuItemClickListener(new SaverMenuItemClickListener(textAnchor));
        return pm;
    }

    private PopupMenu generateCategoryMenu (final EditText textAnchor) {
        PopupMenu pm = new PopupMenu(getActivity(), textAnchor);

        String eBuf = textAnchor.getText().toString();
        eBuf = eBuf.substring(eBuf.lastIndexOf('@') + 1, eBuf.lastIndexOf("."));
        Log.d(TAG, "generateCategoryMenu: Element " + eBuf);

        for (SysElement e : Session.getInstance().getCurrentSystem().getElements()) {
            for (String c : e.getCategories()) {
                pm.getMenu().add(c);
            }
        }

        pm.setOnMenuItemClickListener(new SaverMenuItemClickListener(textAnchor));
        return pm;
    }

    private PopupMenu generateFieldMenu (final EditText textAnchor) {
        PopupMenu pm = new PopupMenu(getActivity(), textAnchor);
        String base = textAnchor.getText().toString();

        // remove last dot
        String eBuf = base.substring(0, base.length()-1);
        String cBuf = eBuf;

        eBuf = eBuf.substring(eBuf.lastIndexOf('@') + 1, eBuf.lastIndexOf("."));
        cBuf = cBuf.substring(cBuf.lastIndexOf(".") + 1, cBuf.length());
        Log.d(TAG, "generateFieldMenu: Element " + eBuf + " and Category " + cBuf);

        for (SysElement e : Session.getInstance().getCurrentSystem().getElements()) {
            if (e.getName().equals(eBuf)) {
                for (String c : e.getCategories()) {
                    if (c.equals(cBuf)) {
                        for (SysField f : e.getFieldsByCategory(c)) {
                            if (f.getType() == Field.FieldType.NUMERIC ||
                                    f.getType() == Field.FieldType.CALCULATED) {
                                pm.getMenu().add(f.getName());
                            }
                        }
                    }
                }
            }
        }

        pm.setOnMenuItemClickListener(new SaverMenuItemClickListener(textAnchor));
        return pm;
    }

    private class SaverMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private EditText textAnchor;

        SaverMenuItemClickListener (EditText textAnchor) {
            this.textAnchor = textAnchor;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String buf = textAnchor.getText().toString() + item.getTitle();
            textAnchor.setText(buf);
            return false;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_edit_field, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_edit_field: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.HELP);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}