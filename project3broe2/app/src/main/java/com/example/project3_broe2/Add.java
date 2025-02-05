package com.example.project3_broe2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add extends AppCompatActivity {

    EditText elem;
    ListView listView;
    InvAdapter invAdapter;
    List<Item> inv;
    ArrayList<String> names;
    DatabaseReference dbr;

    private EditText nameText, priceText, stockText, descrText;
    private boolean deleted = false;
    //final static String ITEM_DESCR = "descr";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.addition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addition), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.mylist);
        inv = new ArrayList<>();
        names = new ArrayList<>();
        invAdapter = new InvAdapter(this, inv);
        listView.setAdapter(invAdapter);

        dbr = FirebaseDatabase.getInstance().getReference("inventory");
        //Task snap = dbr_here.get();
        System.out.println(dbr.get());
        getItems();

        // Param1 - context
        // Param2 - layout for the row


        //nameText, priceText, stockText, descrText
        nameText = findViewById(R.id.itemName);
        priceText = findViewById(R.id.itemPrice);
        stockText = findViewById(R.id.itemStock);
        descrText = findViewById(R.id.itemDescr);
        //c = dbHelper.readAll();

        //myAdapter = new ArrayAdapter<String>(this, R.layout.line);
        //names = new SimpleCursorAdapter(this, R.layout.datalist, c,
        //        columns, new int[]{
        //        R.id.name}, 1);

        //listView.setAdapter(names);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                // CREATE ANOTHER ACTIVITY IDENTICAL TO THIS, BUT IT CAN ONLY DELETE
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    alertView("Single Item Deletion",position);
                                                    return true;
                                                }
                                            }
        );


        //elem =  (EditText)findViewById(R.id.input);
    }

    private void getItems() {
        System.out.println("We in this house");
        //dbr.push();
        //firstPopulate();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("We even more in this house");
                inv.clear();
                System.out.println("We made it into onDataChange!");
                for (DataSnapshot kid : snapshot.getChildren()) {
                    System.out.println(kid);
                    Item item = kid.getValue(Item.class);

                    if (item != null) {
                        //item.setId(kid.getKey());
                        inv.add(item);
                        names.add(item.getItemName());
                    }
                }



                if (!snapshot.exists()) {
                    System.out.println("We made it into Snap not exist!");
                    //firstPopulate();
                }
                System.out.println("We made it to notifyDSC! EDITOR");
                invAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("We out this house");
            }
        });
    }


    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
    }

    protected void onStop(Bundle savedInstanceState) {
        super.onStop();
    }


    ActivityResultLauncher<Intent>
            // LOOK INTO THIS AND FIND OUT HOW TO SEND/RECEIVE
            activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });

    public boolean in_db(String name) {
        for (String entry : names) {
            if (name.equals(entry)) {
                return true;
            }
        }

        return false;
    }

    public void addElem(View v) {
        //String input = elem.getText().toString();
        //nameText, priceText, stockText, descrText
        String name = nameText.getText().toString();
        String descr = descrText.getText().toString();
        String price = priceText.getText().toString();
        String stock = stockText.getText().toString();
        if (in_db(name)) {
            Toast.makeText(getApplicationContext(),"Please only enter a new item.",Toast.LENGTH_SHORT).show();
        }
        else if ((name.length() > 0) && (descr.length() > 0) && (price.length() > 0 && Float.parseFloat(price) <= Float.MAX_VALUE && Float.parseFloat(price) >= Float.MIN_VALUE) && (stock.length() > 0 && Integer.parseInt(stock) <= Integer.MAX_VALUE && Integer.parseInt(stock) >= Integer.MIN_VALUE)) {
            Item newer = new Item(name, Float.valueOf(price).floatValue(), Integer.valueOf(stock).intValue(), descr, 0);
            Map<String, Object> updater = new HashMap<>();
            updater.put("itemName", name);
            updater.put("itemPrice", Float.parseFloat(price));
            updater.put("itemStock", Integer.parseInt(stock));
            updater.put("itemDescr", descr);
            updater.put("itemOrder", 0);
            dbr.child(name).updateChildren(updater);
            finish();
            Intent nextIntent = new Intent(this, Inventory.class);
            activityLauncher.launch(nextIntent);

        }
        else {
            Toast.makeText(getApplicationContext(),"Please fill in all fields.",Toast.LENGTH_SHORT).show();
        }

        /*
        if (input.length() > 0) {
            dbHelper.insert(input, 0.00f, 0);
            Cursor c = dbHelper.readAll();
            mySimAdapter.swapCursor(dbHelper.readAll());
            //myAdapter.add(input);
            Toast.makeText(getApplicationContext(), "Adding " + input, Toast.LENGTH_SHORT).show();
            elem.setText("");
        } else
            Toast.makeText(getApplicationContext(),"Not adding an empty item",Toast.LENGTH_SHORT).show();

         */

    }

    public void deleteElem(View v) {

    }

    public void back(View v) {
        finish();
        Intent nextIntent = new Intent(this, Inventory.class);
        activityLauncher.launch(nextIntent);
    }


    private void alertView(String message ,final int position ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Add.this);
        dialog.setTitle( message )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you want to delete this?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        //dbHelper.delete(mySimAdapter.getItem(position).getName());
                        deleted = true;
                        //bHelper.close();
                        //c.close();
                    }
                }).show();

    }



    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Toast.makeText(getApplicationContext(),"Item 1 chosen",Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.item2) {
            Toast.makeText(getApplicationContext(),"Item 2 chosen",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    */
}