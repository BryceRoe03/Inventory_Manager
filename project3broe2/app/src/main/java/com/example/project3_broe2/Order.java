package com.example.project3_broe2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order extends AppCompatActivity {

    EditText ItemNum;
    TextView OrderText, TotalText;
    ListView listView;
    InvAdapter invAdapter;
    List<Item> inv;
    ArrayList<String> names;
    DatabaseReference dbr;

    Spinner spinner;
    public ArrayList<String> items = new ArrayList<>();
    public ArrayList<Integer> orders = new ArrayList<>();
    public ArrayList<Float> prices = new ArrayList<>();
    public ArrayList<Integer> totals = new ArrayList<>();
    public int pos = 0;
    boolean blump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.order), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.mylist);
        ItemNum = findViewById(R.id.itemName);
        OrderText = findViewById(R.id.itemList);
        TotalText = findViewById(R.id.totalTotal);

        inv = new ArrayList<>();
        names = new ArrayList<>();
        invAdapter = new InvAdapter(this, inv);
        listView.setAdapter(invAdapter);

        dbr = FirebaseDatabase.getInstance().getReference("inventory");
        //Task snap = dbr_here.get();ystem.out.println(dbr.get());

        spinner = findViewById(R.id.spinner);

        //Cursor curse = dbHelper.readAll();




        getItems();



        // Param1 - context
        // Param2 - layout for the row

        //dbHelper.deleteDatabase();

        //c = dbHelper.readAll();


        /*
        SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //System.out.println(i);
        curse.moveToPosition(position);
         */


        //myAdapter = new ArrayAdapter<String>(this, R.layout.line);
        //names = new SimpleCursorAdapter(this, R.layout.datalist, c,
        //        columns, new int[]{
        //        R.id.name}, 1);

        //listView.setAdapter(names);




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
                        if (blump == false) {
                            items.add(item.getItemName());
                        }
                    }
                }

                if (!snapshot.exists()) {
                    System.out.println("We made it into Snap not exist!");
                    //firstPopulate();
                }
                System.out.println("We made it to notifyDSC! EDITOR");
                invAdapter.notifyDataSetChanged();
                if (blump == false) {
                    blump = true;
                    spinnerSpawner();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("We out this house");
            }
        });
    }

    private void spinnerSpawner() {
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(spinAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });


    public void back(View v) {
        finish();
        Intent nextIntent = new Intent(this, MainActivity.class);
        //finish();
        addLauncher.launch(nextIntent);
    }

    public void addOrd(View v) {
        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //System.out.println(i);
        //curse.moveToPosition(pos);
        String name = inv.get(pos).getItemName();
        int stock = inv.get(pos).getItemStock();
        float price = inv.get(pos).getItemPrice();
        int order = inv.get(pos).getItemOrder();

        if (ItemNum.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"Please choose a number",Toast.LENGTH_SHORT).show();
        }
        else {
            int total = Integer.parseInt(ItemNum.getText().toString());
            if (total > stock) {
                Toast.makeText(getApplicationContext(),"More than the stock! Calculating maximum...",Toast.LENGTH_SHORT).show();
                total = stock;
            }
            float calcPrice = price * total;
            String insert = "";
            orders.add(pos);
            prices.add(calcPrice);
            totals.add(total);
            for (int i = 0; i < orders.size(); i++) {
                //curse.moveToPosition(orders.get(i));
                insert += inv.get(orders.get(i)).getItemName() + "(" + totals.get(i) + ")" + " $" + prices.get(i);
                if (i < orders.size()-1) {
                    insert += "\n";
                }
            }
            float totalTotal = 0;
            for (int i = 0; i < prices.size(); i++) {
                totalTotal += prices.get(i);
            }
            OrderText.setText(insert);
            TotalText.setText("TOTAL COST: $" + totalTotal);
            //curse.moveToPosition(pos);
            Map<String, Object> updater = new HashMap<>();
            updater.put("itemStock", stock-total);
            updater.put("itemOrder", order+total);
            dbr.child(name).updateChildren(updater);
            //dbHelper.update(name, price, stock-total, curse.getString(4), order + total);


        }

    }

    public void remove(View v) {
        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //System.out.println(i);
        //curse.moveToPosition(pos);
        String name = inv.get(pos).getItemName();
        int stock = inv.get(pos).getItemStock();
        float price = inv.get(pos).getItemPrice();
        int order = inv.get(pos).getItemOrder();

        if (false) {
            //Toast.makeText(getApplicationContext(),"Please choose a number",Toast.LENGTH_SHORT).show();
        } else {
            int total = 0;
            String insert = "";

            for (int i = orders.size() - 1; i >= 0; i--) {
                if (pos == orders.get(i)) {
                    total += totals.get(i);
                    prices.remove(prices.get(i));
                    totals.remove(totals.get(i));
                    orders.remove(orders.get(i));
                }
            }
            for (int i = 0; i < orders.size(); i++) {
                //curse.moveToPosition(orders.get(i));
                insert += inv.get(orders.get(i)).getItemName() + "(" + totals.get(i) + ")" + " $" + prices.get(i);
                if (i < orders.size()-1) {
                    insert += "\n";
                }
            }
            float totalTotal = 0;
            for (int i = 0; i < prices.size(); i++) {
                totalTotal += prices.get(i);
            }
            OrderText.setText(insert);
            TotalText.setText("TOTAL COST: $" + totalTotal);
            //curse.moveToPosition(pos);
            //dbHelper.update(name, price, stock + total, curse.getString(4), 0);
            Map<String, Object> updater = new HashMap<>();
            updater.put("itemStock", stock+total);
            updater.put("itemOrder", 0);
            dbr.child(name).updateChildren(updater);

        }
    }

    public void calc() {

    }


    public void finish(View v) {
        back(v);
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

