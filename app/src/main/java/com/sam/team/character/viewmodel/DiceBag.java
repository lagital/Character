package com.sam.team.character.viewmodel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import static com.sam.team.character.viewmodel.DrawerItem.DrawerItemType.DICEBAG;
import static com.sam.team.character.viewmodel.DrawerItem.DrawerItemType.PAGE;

/**
 * Container for dice rolling.
 * Created by lagital on 22.01.17.
 */

@Root(name = "DiceBag")
public class DiceBag implements DrawerItem {
    private static final String TAG = "DiceBag";

    @Element(name = "Quantity") private int quantity;
    @Element(name = "Power")    private int power;
    private Random rand = new Random();


    public DiceBag() {}
    public DiceBag(int number, int power) {
        this.quantity = number;
        this.power = power;
    }

    public ArrayList<Integer> shake() {
        ArrayList<Integer> results  = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            results.add(rand.nextInt((power - 1) + 1) + 1);
        }
        return results;
    }

    public String shakeAsString() {
        String s = "";
        int sum = 0;
        for (Integer i : shake()) {
            s += Integer.toString(i) + " + ";
            sum += i;
        }
        s = s.substring(0, s.lastIndexOf("+ "));
        s += "= " + Integer.toString(sum);
        return s;
    }

    public String getDescription() {
        return Integer.toString(quantity)
                + "xD"
                + Integer.toString(power);
    }

    @Override
    public DrawerItemType getDrawerItemType() {
        return DICEBAG;
    }

    @Override
    public String getDrawerItemTitle() {
        return getDescription();
    }
}