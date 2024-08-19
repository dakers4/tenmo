package com.techelevator;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.Objects;  // put this in to match model
import java.util.*; //took it out as it shows as not being needed

// OVERALL NOTE: in original - there was no location information in the .txt, just partNo so the parts variables here didn't have any location info.
//  ------------ we're using the equivalent of bin location with our slot location.  Make sure to trace logic on file import loops.
public class Item {

    public Item (){}; //added default constructor - not sure if needed (it wasn't in orig
    private String slot;  // partNo in orig.  ex. A1// named Location in .txt
    private String itemType;  // desc in orig; ex. Chips, Gum, Candy, Drink
    private String itemName;      // name in orig; Cowtales, Wonka Bar, Triplemint, Dr. Salt
    private Double price;       // pennyPrice in orig.potentially change int to big decimal //In orig this was int since no decimal prices


    //GETTERS

    public String getSlot () {
        return slot;
    }
    public String getItemType () {
        return itemType;
    }
    public String getItemName () {
        return itemName;
    }
    public Double getPrice () {
        return price;
    }

    // CONSTRUCTOR FOR ITEM
    public Item(String slot, String itemName, Double price, String itemType) {
        this.slot = slot;
        this.itemName = itemName;
        this.price = price;
        this.itemType = itemType;
    }

    @Override
    public String toString () {

        return slot + ", " + itemName + ", " + price + ", " + itemType; // taking out '\n'

    }

//note to Dylan - added back both overrides blow.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return price == item.price && slot.equals(item.slot) && itemName.equals(item.itemName) && itemType.equals(item.itemType);
    }

    //note - i think the below was something we originally removed, but it is likely necessary to have this because of other Classes use of hashcode.  Question - what are we overriding?

    @Override
    public int hashCode() {
        return Objects.hash(slot, itemName, itemType, price);
    }


}
