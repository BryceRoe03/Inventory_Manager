package com.example.project3_broe2;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String id, itemName, itemDescr;
    private float itemPrice;
    private int itemStock, itemOrder;

    public Item() {}

    public Item(String itemName, float itemPrice, int itemStock, String itemDescr, int itemOrder) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemStock = itemStock;
        this.itemDescr = itemDescr;
        this.itemOrder = itemOrder;
    }

    public void insert(String itemName, float itemPrice, int itemStock, String itemDescription, int itemOrder) {
        DatabaseReference invRef = FirebaseDatabase.getInstance().getReference("/project3-broe2/firebasestorage/app/inventory");
        String id = invRef.push().getKey();

        Item item = new Item(itemName, itemPrice, itemStock, itemDescription, itemOrder);
        invRef.child(id).setValue(item);
    }

    public void delete(String id) {
        DatabaseReference invRef = FirebaseDatabase.getInstance().getReference("/project3-broe2/firebasestorage/app/inventory");
        invRef.child(id).removeValue();
    }

    public void update(String id, String itemName, float itemPrice, int itemStock, String itemDescr, int itemOrder) {
        DatabaseReference invRef = FirebaseDatabase.getInstance().getReference("/project3-broe2/firebasestorage/app/inventory");

        Map<String, Object> update = new HashMap<>();
        update.put("itemName", itemName);
        update.put("itemPrice", itemPrice);
        update.put("itemStock", itemStock);
        update.put("itemDescription", itemDescr);
        update.put("itemOrder", itemOrder);

        invRef.child(id).updateChildren(update);
    }

    public String getId() {
        return this.id;
    }

    public String getItemName() {
        return this.itemName;
    }

    public float getItemPrice() {
        return this.itemPrice;
    }

    public int getItemStock() {
        return this.itemStock;
    }

    public String getItemDescr() {
        return itemDescr;
    }

    public int getItemOrder() {
        return this.itemOrder;
    }

    public Task<Item> getItem(String id) {
        DatabaseReference invRef = FirebaseDatabase.getInstance().getReference("inventory");

        return invRef.get().continueWith(task -> {

            DataSnapshot snapshot = task.getResult();
            Item item = snapshot.getValue(Item.class);

            if (item != null) {
                //item.setId(snapshot.getKey());
            }

            return item;
        });
    }
}
