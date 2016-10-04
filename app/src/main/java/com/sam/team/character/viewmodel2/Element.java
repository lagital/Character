package com.sam.team.character.viewmodel2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class Element extends BaseObservable implements Parcelable {

    private static final String TAG = "Element";

    private String name, type;
    private TreeMap<String, TreeMap<String, Field>> fields;

    //constructors
    public Element() {
        name = type = null;
        fields = new TreeMap<String, TreeMap<String, Field>>();
    }

    public Element(String name, String type) {
        this.name = name;
        this.type = type;
        fields = new TreeMap<String, TreeMap<String, Field>>();
    }

    //work with name
    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    //work with type
    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getType() {
        return type; 
    }

    //work with fields
    public void addField(Field field) {
        if (field.getCategory() == null || field.getName() == null) return;
        if (!fields.containsKey(field.getCategory())) {
            TreeMap<String, Field> temp = new TreeMap<String, Field>();
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
        } else {
            TreeMap<String, Field> temp = fields.get(field.getCategory());
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
        }
    }

    public void removeField(String type, String name) {
        if (fields.containsKey(type)) {
            TreeMap<String, Field> temp = fields.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }

    public Field getField(String category, String name) {
        return fields.get(category).get(name);
    }

    public Collection getFieldsByCategory(String type) {
        if (fields.containsKey(type)) {
            return fields.get(type).values();
        } else {
            return null;
        }
    }

    //work with cetegories
    public Set getCategories() {
        return fields.keySet();
    }

    public Set getNamesByCategory(String category) {
        if (fields.containsKey(category)) {
            return fields.get(category).keySet();
        } else {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    // object into Parcel
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(TAG, "writeToParcel");
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeMap(fields);
    }

    public static final Parcelable.Creator<Element> CREATOR = new Parcelable.Creator<Element>() {
        // object from Parcel
        public Element createFromParcel(Parcel in) {
            Log.d(TAG, "createFromParcel");
            return new Element(in);
        }

        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    // constructor from Parcel
    private Element(Parcel parcel) {
        Log.d(TAG, "Element from Parcel");
        name = parcel.readString();
        type = parcel.readString();
        parcel.readMap(fields, ClassLoader.getSystemClassLoader());
    }
}