package com.sam.team.character.design;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Dialog builder.
 * Created by pborisenko on 11/26/2016.
 */

class TextParmsDialogBuilder extends AlertDialog.Builder{
    private static final String TAG = "TextParmsDialogBuilder";

    private AlertDialog dialog;
    private ArrayList<String> results = new ArrayList<>();
    private ArrayList<TextParameter> parameters;
    private Context context;
    private LinearLayout l;

    TextParmsDialogBuilder (final Context context, int containerRes,
                                   final int parameterRes, int titleRes,
                                   final ArrayList<TextParameter> parameters) {
        super(context);
        // construct view for dialog
        l = (LinearLayout) View.inflate(context, containerRes, null);
        LayoutInflater li = LayoutInflater.from(context);
        this.parameters = parameters;
        this.context = context;

        for (final TextParameter p : parameters) {
            switch (p.getMode()) {
                case LIST: {
                    ListViewCompat lv = (ListViewCompat) li.inflate(R.layout.text_parameter_list, null);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            results.add(Arrays.asList(p.getValueList()).get(position));
                            dialog.dismiss();
                        }
                    });
                    //once we face "LIST" we ignore other parameters
                    l.removeAllViewsInLayout();
                    l.addView(lv);
                    this.setView(l);
                    this.setTitle(titleRes);
                    this.show();
                    return;
                }
                case SINGLE: {
                    EditTextEndCursor e = (EditTextEndCursor) li.inflate(R.layout.text_parameter, null);
                    e.setText(p.getCurrentValue());

                    if (p.isMandatory()) {
                        e.setTypeface(null, Typeface.BOLD);
                    }

                    Log.d(TAG, e.getText().toString());
                    e.setOnTouchListener(new CleanOnTouchListener(e, p.getDfltValue()));
                    l.addView(e);
                    break;
                }
                case DROP_DOWN: {
                    AppCompatSpinner e = (AppCompatSpinner) li.inflate(R.layout.text_parameter_drop_down, null);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, p.getValueList());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    e.setAdapter(adapter);
                    l.addView(e);
                    break;
                }
            }
        }
        constructButtons();
        this.setView(l);
        this.setTitle(titleRes);
    }

    AlertDialog getDialog() {
        return dialog;
    }

    void applySettings () {
        Log.d(TAG, "applySettings");
        // for overriding in particular cases
    }

    private void constructButtons() {
        this.setPositiveButton(context.getResources().getString(R.string.dialog_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing, see dialog.getButton
                    }
                });

        this.setNegativeButton(context.getResources().getString(R.string.dialog_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog = this.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                results.clear();
                // validate each parameter
                boolean validSettings = true;
                for (int i = 0; i < parameters.size(); i++) {
                    String s;
                    if (parameters.get(i).getValueList() != null) {
                        AppCompatSpinner e = (AppCompatSpinner) l.getChildAt(i);
                        s = e.getSelectedItem().toString();
                    } else {
                        EditText e = (EditText) l.getChildAt(i);
                        s = e.getText().toString();
                    }

                    if (s.isEmpty() || s.equalsIgnoreCase(parameters.get(i).getDfltValue())) {
                        if (parameters.get(i).isMandatory()) {
                            validSettings = false;
                        }
                    }
                    results.add(s);
                }

                if (!validSettings) {
                    Toast.makeText(context,
                            context.getResources().getString(R.string.dialog_mandatory_parms_missing),
                            Toast.LENGTH_SHORT).show();
                } else {
                    applySettings();
                    dialog.cancel();
                }
            }
        });
    }

    ArrayList<String> getResults() {
        return results;
    }
}
