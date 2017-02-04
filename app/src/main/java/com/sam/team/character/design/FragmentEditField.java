package com.sam.team.character.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelField;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.sam.team.character.core.SB_Field.FieldType.CALCULATED;
import static com.sam.team.character.core.SB_Field.FieldType.LONG_TEXT;
import static com.sam.team.character.core.SB_Field.FieldType.NUMERIC;
import static com.sam.team.character.core.SB_Field.FieldType.SHORT_TEXT;
import static com.sam.team.character.core.SB_Field.FieldType.UNDEFINED;
import static com.sam.team.character.design.ActivityFieldPicker.RESULT_FIELD;

/**
 * Fragment to configure or edit Field.
 * Created by pborisenko on 11/5/2016.
 */

public class FragmentEditField extends Fragment {

    private static final String TAG = "FragmentEditField";

    private ViewModelField field;

    @BindView(R.id.stage_name) LinearLayout nameContainer;
    @BindView(R.id.stage_type) LinearLayout typeContainer;
    @BindView(R.id.stage_value) LinearLayout valueContainer;

    @BindView(R.id.stage_name_text) AppCompatEditText editTextName;
    @BindView(R.id.stage_value_text) AppCompatEditText editTextValue;

    @BindView(R.id.stage_type_picker) NumberPicker pickerType;

    @BindView(R.id.btn_add_link) ImageView btnAddLink;
    @BindView(R.id.btn_add_mention) ImageView btnAddMention;
    @BindView(R.id.btn_ok) Button btnOK;
    @BindView(R.id.btn_cancel) Button btnCancel;

    private NumberPicker.OnValueChangeListener pickerListener;

    private Step nameStep;
    private Step typeStep;
    private Step valueStep;
    private Step btnOKStep;

    private Integer currentTypeInt;
    private String currentOpenSymbol;
    private String currentCloseSymbol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_field, null);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_field_title);

        Step.setTransitionTime(getResources().getInteger(R.integer.step_transition_time));

        /*----------------------------------- NAME STEP -----------------------------------*/
        nameStep = new Step(nameContainer) {
            @Override
            boolean prevalidate() {
                return isNameValid();
            }
        };

        editTextName.setOnTouchListener(new CleanOnTouchListener(editTextName,
                getActivity().getResources().getString(R.string.edit_field_dflt_name)));
        editTextName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String str = editTextName.getText().toString();
                Log.d(TAG, "afterTextChanged to " + str);
                nameStep.validate();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /*----------------------------------- TYPE STEP -----------------------------------*/
        pickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "Picker - onValueChange " + Integer.toString(newVal));
                currentTypeInt = newVal;
                typeStep.validate();
            }
        };
        typeStep = new Step(typeContainer) {
            @Override
            boolean prevalidate() {
                return isTypeValid();
            }
        };

        String[] stringTypes = new String[] {
                ViewModelField.formatTypeToName(getActivity(), UNDEFINED),
                ViewModelField.formatTypeToName(getActivity(), SHORT_TEXT),
                ViewModelField.formatTypeToName(getActivity(), LONG_TEXT),
                ViewModelField.formatTypeToName(getActivity(), NUMERIC),
                ViewModelField.formatTypeToName(getActivity(), CALCULATED)
        };
        pickerType.setMinValue(0);
        pickerType.setValue(0);
        pickerType.setMaxValue(stringTypes.length - 1);
        pickerType.setWrapSelectorWheel(false);
        pickerType.setDisplayedValues(stringTypes);
        pickerType.setOnValueChangedListener(pickerListener);

        /*----------------------------------- VALUE STEP -----------------------------------*/
        valueStep = new Step(valueContainer) {
            @Override
            boolean prevalidate() {
                return isValueValid();
            }

            @Override
            void enable() {
                super.enable();
                if (ViewModelField.formatIntToType(currentTypeInt) == CALCULATED) {
                    btnAddLink.setVisibility(View.VISIBLE);
                    btnAddMention.setVisibility(View.GONE);
                } else if (ViewModelField.formatIntToType(currentTypeInt) == SHORT_TEXT ||
                        ViewModelField.formatIntToType(currentTypeInt) == LONG_TEXT) {
                    btnAddMention.setVisibility(View.VISIBLE);
                    btnAddLink.setVisibility(View.GONE);
                } else {
                    btnAddMention.setVisibility(View.GONE);
                    btnAddLink.setVisibility(View.GONE);
                }
            }
        };

        editTextValue.setOnTouchListener(new CleanOnTouchListener(editTextValue,
                getActivity().getResources().getString(R.string.edit_field_dflt_value)));

        editTextValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = editTextValue.getText().toString();
                int dotCounter;
                Log.d(TAG, "Value - onTextChanged " + str);
                valueStep.validate();

                // select element name if user begins typing Mention or Link
                if (before == 0 &&
                        (str.endsWith(ViewModelField.MENTION_OPEN_SYMBOL) || str.endsWith(ViewModelField.LINK_OPEN_SYMBOL))) {
                    currentOpenSymbol = str.substring(str.length() - 1);
                    Log.d(TAG, "Open symbol: " + currentOpenSymbol);
                    Log.d(TAG, ViewModelField.formatIntToType(currentTypeInt).toString());

                    if (currentOpenSymbol.equals(ViewModelField.LINK_OPEN_SYMBOL) &&
                            ViewModelField.formatIntToType(currentTypeInt).equals(CALCULATED)) {
                        generateElementMenu(editTextValue).show();
                    }
                    if (currentOpenSymbol.equals(ViewModelField.MENTION_OPEN_SYMBOL) &&
                            !ViewModelField.formatIntToType(currentTypeInt).equals(CALCULATED) &&
                            !ViewModelField.formatIntToType(currentTypeInt).equals(NUMERIC)) {
                        generateElementMenu(editTextValue).show();
                    }
                }

                if (before == 0 && str.endsWith(ViewModelField.DELIMITER)) {
                    str = str.substring(str.lastIndexOf(currentOpenSymbol) + 1, str.length() - 1);
                    // all between last "{" or "[" and current dot
                    dotCounter = str.length() - str.replace(ViewModelField.DELIMITER, "").length();

                    if (valueStep.isValid()) {
                        // no previous dots - select category
                        if (dotCounter == 0) {
                           generateCategoryMenu(editTextValue).show();
                        }
                        // one previous dot - select field
                        if (dotCounter == 1) {
                            generateFieldMenu(editTextValue).show();
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable e) {}
        });

        btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnAddLink onClick");
                currentOpenSymbol = ViewModelField.LINK_OPEN_SYMBOL;
                currentCloseSymbol = ViewModelField.LINK_CLOSE_SYMBOL;
                Intent intent = new Intent(FragmentEditField.this.getActivity(), ActivityFieldPicker.class);
                String[] tmp = {CALCULATED.name()};
                intent.putExtra(ActivityFieldPicker.EXTRA_FILTER, tmp);
                startActivityForResult(intent, ActivityFieldPicker.REQUEST_CODE);
            }
        });

        btnAddMention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnAddMention onClick");
                currentOpenSymbol = ViewModelField.MENTION_OPEN_SYMBOL;
                currentCloseSymbol = ViewModelField.MENTION_CLOSE_SYMBOL;
                Intent intent = new Intent(FragmentEditField.this.getActivity(), ActivityFieldPicker.class);
                String[] tmp = {SHORT_TEXT.name(), LONG_TEXT.name()};
                intent.putExtra(ActivityFieldPicker.EXTRA_FILTER, tmp);
                startActivityForResult(intent, ActivityFieldPicker.REQUEST_CODE);
            }
        });

        /*---------------------------------- BUTTONS --------------------------------------*/
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnOK onClick");
                if (field != null) {
                    field.setName(editTextName.getText().toString());
                    field.setType(ViewModelField.formatIntToType(currentTypeInt));
                    field.setValue(editTextValue.getText().toString());
                } else {
                    createFieldFromSettings();
                }
                getFragmentManager().popBackStack();
            }
        });

        btnOKStep = new Step(btnOK) {
            @Override
            void enable() {
                btnOK.setVisibility(View.VISIBLE);
            }

            @Override
            void disable() {
                btnOK.setVisibility(View.INVISIBLE);
            }
        };

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnCancel onClick");
                FragmentEditField.this.getActivity().onBackPressed();
            }
        });

        /*--------------------------------- RELATIONS -------------------------------------*/
        nameStep.addControlChildren(new Step[]{typeStep});
        typeStep.addControlChildren(new Step[]{valueStep});
        valueStep.addControlChildren(new Step[]{btnOKStep});

        /*
         * Edit mode for existing field
        */
        field = Session.getInstance().getFieldFromCache();
        if (field != null) {
            createSettingsFromField();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(field.getName());
        }

        return view;
    }

    private void createFieldFromSettings() {
        Log.d(TAG, "createFieldFromSettings");
        Session.getInstance().getSystemFromCache().addField(
                Session.getInstance().getElementFromCache().getName(),
                Session.getInstance().getCategoryFromCache().getName(),
                editTextName.getText().toString(),
                ViewModelField.formatIntToType(currentTypeInt)
        );
        ViewModelField tmp = new ViewModelField();
        try {
            tmp = Session.getInstance().getSystemFromCache().getField(
                  Session.getInstance().getElementFromCache().getName(),
                  Session.getInstance().getCategoryFromCache().getName(),
                  editTextName.getText().toString());
            tmp.setValue(editTextValue.getText().toString());
        } catch(Exception e) {}
    }

    private void createSettingsFromField() {
        Log.d(TAG, "createSettingsFromField");

        editTextName.setText(field.getName());
        currentTypeInt = ViewModelField.formatTypeToInt(field.getType());
        if (currentTypeInt.equals(null)) {
            currentTypeInt = 0;
        }
        pickerType.setValue(currentTypeInt);
        pickerListener.onValueChange(pickerType, 0, currentTypeInt);
        editTextValue.setText(field.getValue());
    }

    private PopupMenu generateElementMenu(final EditText textAnchor) {
        Log.d(TAG, "generateElementMenu - " + textAnchor.getText().toString());
        PopupMenu pm = new PopupMenu(getActivity(), textAnchor);

        for (String e : Session.getInstance().getSystemFromCache().getElementNames()) {
            pm.getMenu().add(e);
        }

        pm.setOnMenuItemClickListener(new SaverMenuItemClickListener(textAnchor));
        return pm;
    }

    private PopupMenu generateCategoryMenu (final EditText textAnchor) {
        PopupMenu pm = new PopupMenu(getActivity(), textAnchor);

        String eBuf = textAnchor.getText().toString();
        eBuf = eBuf.substring(eBuf.lastIndexOf(currentOpenSymbol) + 1, eBuf.lastIndexOf(ViewModelField.DELIMITER));
        Log.d(TAG, "generateCategoryMenu: Element " + eBuf);

        for (String c : Session.getInstance().getSystemFromCache()
        .getElement(eBuf).getCategoryNames()) {
            pm.getMenu().add(c);
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

        eBuf = eBuf.substring(eBuf.lastIndexOf(currentOpenSymbol) + 1, eBuf.lastIndexOf(ViewModelField.DELIMITER));
        cBuf = cBuf.substring(cBuf.lastIndexOf(ViewModelField.DELIMITER) + 1, cBuf.length());
        Log.d(TAG, "generateFieldMenu: Element " + eBuf + " and Category " + cBuf);

        for (String f : Session.getInstance().getSystemFromCache().getFieldsInCategory(eBuf, cBuf)) {
            ViewModelField tmp = new ViewModelField();
            try {
                Session.getInstance().getSystemFromCache().getField(eBuf, cBuf, f);
                if (currentOpenSymbol.equals(ViewModelField.LINK_OPEN_SYMBOL) &&
                        ViewModelField.formatIntToType(currentTypeInt) == CALCULATED) {
                    if (tmp.isLinkCompatible()) {
                        pm.getMenu().add(f);
                    }
                } else {
                    pm.getMenu().add(f);
                }
            } catch(Exception e) {}
        }

        // after Field name we need to close "[" with "]" or "{" with "}"
        pm.setOnMenuItemClickListener(new SaverMenuItemClickListener(textAnchor) {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                super.onMenuItemClick(item);
                if (currentOpenSymbol.equals(ViewModelField.LINK_OPEN_SYMBOL)) {
                    textAnchor.setText(textAnchor.getText() + ViewModelField.LINK_CLOSE_SYMBOL);
                }
                if (currentOpenSymbol.equals(ViewModelField.MENTION_OPEN_SYMBOL)) {
                    textAnchor.setText(textAnchor.getText() + ViewModelField.MENTION_CLOSE_SYMBOL);
                }
                return false;
            }
        });
        return pm;
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

    boolean isNameValid() {
        Log.d(TAG, "isNameValid");
        return CleanOnTouchListener.isValidString(this.getActivity(),
                editTextName.getText().toString(), R.string.edit_field_dflt_name);
    }

    boolean isTypeValid() {
        Log.d(TAG, "isTypeValid");
        return pickerType.getValue() != 0;
    }

    boolean isValueValid() {
        Log.d(TAG, "isValueValid");
        return CleanOnTouchListener.isValidString(this.getActivity(),
                editTextValue.getText().toString(), R.string.edit_field_dflt_value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityFieldPicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(RESULT_FIELD)) {
                    editTextValue.setText(editTextValue.getText() +
                            currentOpenSymbol +
                            data.getStringExtra(RESULT_FIELD) +
                            currentCloseSymbol);
                    editTextValue.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }
    }
}