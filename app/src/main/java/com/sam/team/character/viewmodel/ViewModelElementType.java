package com.sam.team.character.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.sam.team.character.corev2.SB_ElementType;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelElementType extends BaseObservable implements ListItem, ViewModelEnvelope<SB_ElementType> {

    private static final String TAG = "ViewModelElementType";

    private SB_ElementType elementType;

    public ViewModelElementType(SB_ElementType elementType) {
        this.elementType = elementType;
    }

    @Override
    public SB_ElementType getContent() {
        return elementType;
    }

    @Override
    public void setContent(SB_ElementType elementType) {
        this.elementType = elementType;
    }

    @Override
    public void save() {}

    @Override
    public void delete() {}

    @Bindable
    public String getName() {
        return elementType.getName();
    }

    public void setName(String name) {
        elementType.setName(name);
    }

    public ArrayList<String> getCategories () {
        return elementType.getCategories();
    }

    @Override
    public int getItemType() {
        return TYPE_ELEMENT;
    }
}
