package com.example.project3_broe2;

import static java.lang.Thread.sleep;

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

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Edit_Screen extends AppCompatActivity {

    EditText elem;
    ListView listView;
    InvAdapter invAdapter;
    List<Item> inv;
    DatabaseReference dbr_here;
    //ArrayAdapter myAdapter;
    //SimpleCursorAdapter mySimAdapter;
    private EditText nameText, priceText, stockText, descrText;
    private TextView editName;
    //final static String ITEM_DESCR = "descr";
    public boolean deleted = false;
    int pos;
    android.app.AlertDialog.Builder del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.mylist);
        inv = new ArrayList<>();
        invAdapter = new InvAdapter(this, inv);
        listView.setAdapter(invAdapter);
        //dbr = FirebaseDatabase.getInstance().getReference();

        //dbr.keepSynced(true);
        //System.out.println(dbr);
        //getItems();
        dbr_here = FirebaseDatabase.getInstance().getReference("inventory");
        //Task snap = dbr_here.get();
        System.out.println(dbr_here.get());

        getItems();

        // Param1 - context
        // Param2 - layout for the row
        //nameText, priceText, stockText, descrText
        //nameText = findViewById(R.id.itemName);
        priceText = findViewById(R.id.itemPrice);
        stockText = findViewById(R.id.itemStock);
        descrText = findViewById(R.id.itemDescr);
        editName = findViewById(R.id.edit_name);
        //c = dbHelper.readAll();




        //Edit_Screen.LoadDB load = new Edit_Screen.LoadDB();
        //load.execute(dbHelper);
        //Map<String, Item> invStart = new HashMap<>();
        //dbr.setValue(invStart);

        //Intent intentGet = getIntent();
        //pos = Integer.parseInt(intentGet.getStringExtra(Inventory.POSITION));
        //System.out.println(inv);





        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //curse.moveToPosition(pos);
        //String entry = cursorCourses.getString()



        //myAdapter = new ArrayAdapter<String>(this, R.layout.line);
        //names = new SimpleCursorAdapter(this, R.layout.datalist, c,
        //        columns, new int[]{
        //        R.id.name}, 1);

        //listView.setAdapter(names);
        del = new android.app.AlertDialog.Builder(Edit_Screen.this);
        del.setCancelable(true);

        del.setTitle("Delete?");
        del.setMessage("Are you sure you want to delete this?");

        del.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        del.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteElem();
            }
        });


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
        dbr_here.addValueEventListener(new ValueEventListener() {
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
                    }
                }
                if (deleted == false) {
                    displayItem();
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

    public void firstPopulate() {
        //DatabaseReference invRef = dbr.child("inventory");
        //DatabaseReference newInvRef = dbr.push();
        Map<String, Item> invStart = new HashMap<>();
        invStart.put("Mega Mushroom", new Item("Mega Mushroom", 5.99f, 21, "Stomp all the competition!", 0));
        invStart.put("Power Star", new Item("Power Star", 999.99f, 1, "Blast your enemies to the stars!", 0));
        invStart.put("Blooper", new Item("Blooper", 65.88f, 14, "Blot out those who stand in your way!", 0));

        dbr_here.setValue(invStart);
    }

        /*
        private final class LoadDB extends AsyncTask<Database, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
            System.out.println("In Post Execute!");
            // "Cost per item: $" + thingy
            // thingy + " in stock"
            mySimAdapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.datalist,
                    data,
                    columns,
                    new int[]{R.id.name, R.id.price, R.id.stock, R.id.descr},0);
            System.out.println("Made it past mySimAdapter!");
            c = data;
            listView.setAdapter(mySimAdapter);
        }
        // runs on its own thread
        @Override
        protected Cursor doInBackground(Database... dbHelp) {
            //dbHelper = new DatabaseOpenHelper(dbHelp);
            return dbHelper.readAll();
            //db = dbHelper.getWritableDatabase();
            //return db.query(dbHelper.NAME, columns, null, null,
            //        null, null, null);
        }
    }

         */



    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
        //c = dbHelper.readAll();
        //c.close();
    }

    protected void onStop(Bundle savedInstanceState) {
        super.onStop();
        //db.close();
        //c.close();
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

    private void displayItem() {
        Intent intentGet = getIntent();
        pos = Integer.parseInt(intentGet.getStringExtra(Inventory.POSITION));
        Item selected = inv.get(pos);
        editName.setText(selected.getItemName());
        priceText.setHint(String.valueOf(selected.getItemPrice()));
        stockText.setHint(String.valueOf(selected.getItemStock()));
        descrText.setHint(selected.getItemDescr());
    }
    public void updateElem(View v) {
        //String input = elem.getText().toString();
        //nameText, priceText, stockText, descrText
        //String name = nameText.getText().toString();

        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //curse.moveToPosition(pos);
        //String entry = cursorCourses.getString()

        //String name = (curse.getString(1));
        String descr = descrText.getText().toString();
        String price = priceText.getText().toString();
        String stock = stockText.getText().toString();
        if (descr.length() <= 0) {
            Item select = inv.get(pos);
            descr = select.getItemDescr();
        }
        if (price.length() <= 0) {
            Item select = inv.get(pos);
            price = String.valueOf(select.getItemPrice());
        }
        if (stock.length() <= 0) {
            Item select = inv.get(pos);
            stock = String.valueOf(select.getItemStock());
        }
        //dbHelper.update(name, Float.valueOf(price).floatValue(), Integer.valueOf(stock).intValue(), descr, 0);
        //Cursor c = dbHelper.readAll();
        //mySimAdapter.swapCursor(dbHelper.readAll());
        Item select = inv.get(pos);
        String name = select.getItemName();
        select = inv.get(pos);
        int order = select.getItemOrder();
        Map<String, Object> updater = new HashMap<>();
        updater.put("itemName", name);
        updater.put("itemPrice", Float.parseFloat(price));
        updater.put("itemStock", Integer.parseInt(stock));
        updater.put("itemDescr", descr);
        updater.put("itemOrder", order);
        dbr_here.child(name).updateChildren(updater);
        finish();
        Intent nextIntent = new Intent(this, Inventory.class);
        activityLauncher.launch(nextIntent);


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
    public void doDelete(View v) {
        del.show();
    }
    public void deleteElem() {
        //alertView("Single Item Deletion",pos);
        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        //System.out.println(i);
        //curse.moveToPosition(pos);
        //dbHelper.delete(curse.getString(1));
        //myAdapter.remove(myAdapter.getItem(position));
        //Cursor c = dbHelper.readAll();
        //mySimAdapter.swapCursor(dbHelper.readAll());
        dbr_here.child(inv.get(pos).getItemName()).removeValue();
        deleted = true;


        if (deleted) {
            finish();
            Intent nextIntent = new Intent(this, Inventory.class);
            activityLauncher.launch(nextIntent);
        }
    }

    public void back(View v) {
        finish();
        //startActivity(getIntent());
    }


    private void alertView(String message ,final int position ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Edit_Screen.this);
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
