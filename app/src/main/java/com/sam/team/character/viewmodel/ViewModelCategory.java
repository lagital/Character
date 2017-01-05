package com.sam.team.character.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.sam.team.character.corev2.SB_Category;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class ViewModelCategory extends BaseObservable implements ListItem, ViewModelEnvelope<SB_Category> {

    private SB_Category category;

    public ViewModelCategory(SB_Category category) {
        this.category = category;
    }

    @Override
    public SB_Category getContent() {
        return category;
    }

    @Override
    public void setContent(SB_Category category) {
        this.category = category;
    }

    @Override
    public void save() {}

    @Override
    public void delete() {}

    @Bindable
    public String getName() {
        return category.getName();
    }

    public void setName(String name) { category.setName(name);}

    @Override
    public int getItemType() {
        return TYPE_CATEGORY;
    }
}
