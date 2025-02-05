package com.example.project3_broe2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.firebase.ui.database.FirebaseListOptions;
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

public class Inventory extends AppCompatActivity {

    EditText elem;
    ListView listView;
    InvAdapter invAdapter;
    List<Item> inv;
    DatabaseReference dbr;


    public final static String POSITION = "com.example.project3_broe2.Inventory.pos";

    //final static String[] columns = {TASK_NAME, ITEM_PRICE, ITEM_STOCK, ITEM_DESCR, ITEM_ORDER};

    //private Database dbHelper = null;
    //Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inventory), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.mylist);

        inv = new ArrayList<>();
        invAdapter = new InvAdapter(this, inv);
        listView.setAdapter(invAdapter);

        dbr = FirebaseDatabase.getInstance().getReference("inventory");
        getItems();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selected = inv.get(position);
                String topic1 = selected.getItemName();
                selected = inv.get(position);
                String topic2 = String.valueOf(selected.getItemDescr());



                Snackbar.make(view, topic1 +": "+ topic2, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                /*
                System.out.println(mySimAdapter.getItem(position).toString());
                SQLiteDatabase dbt = dbHelper.getReadableDatabase();
                Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
                curse.moveToPosition(position);
                //String entry = cursorCourses.getString()
                String topic1 = curse.getString(1);
                System.out.println(topic1);
                String topic2 = curse.getString(4);
                System.out.println(topic2);
                //Toast.makeText(getApplicationContext(), "Item " + topic, Toast.LENGTH_SHORT).show();
                String text = topic1;
                String descr = topic2;

                Snackbar.make(view, text +": "+ descr, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                */

                /*
                if (!topic.startsWith("Done: ")) {

                    System.out.println("FORT");
                    dbHelper.update(text, ("Done: " + text));
                    System.out.println("NITE");
                    Cursor c = dbHelper.readAll();
                    mySimAdapter.swapCursor(dbHelper.readAll());
                    //myAdapter.add(("Done: " + myAdapter.getItem(position)));
                    //myAdapter.remove(myAdapter.getItem(position));
                    //(myAdapter.getItem(position));
                    //((TextView) view).setText(("Done: " + ((TextView) view).getText()));
                }
                else {
                    //String text = ((TextView) view).getText().toString();
                    dbHelper.update(text, text.substring(6));
                    Cursor c = dbHelper.readAll();
                    mySimAdapter.swapCursor(dbHelper.readAll());
                    //String text = (String)myAdapter.getItem(position);
                    //myAdapter.remove(myAdapter.getItem(position));
                    //myAdapter.insert(text.substring(6), 0);
                }
                curse.close();
                */
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                // CREATE ANOTHER ACTIVITY IDENTICAL TO THIS, BUT IT CAN ONLY DELETE
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Item selected = inv.get(position);
                                                    edit_sender(view, position);
                                                    //alertView("Single Item Deletion",position);

                                                    return true;
                                                }
                                            }
        );
        //elem =  (EditText)findViewById(R.id.input);
    }


    private void getItems() {
        //firstPopulate();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                if (!snapshot.exists()) {
                    System.out.println("We made it into Snap not exist!");
                    firstPopulate();
                }
                System.out.println("We made it to notifyDSC!");
                invAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        dbr.setValue(invStart);
    }

    public void edit_sender(View v, int position) {
        Intent nextIntent = new Intent(this, Edit_Screen.class);
        //FirebaseDatabase.getInstance().goOffline();
        finish();
        nextIntent.putExtra(POSITION, "" + position);
        addLauncher.launch(nextIntent);
    }

    protected void onResume(Bundle savedInstanceState) {
        super.onResume();
        //FirebaseDatabase.getInstance().goOnline();
    }

    protected void onStop(Bundle savedInstanceState) {
        super.onStop();
    }

    public void clearDone(View v) {
        /*
        SQLiteDatabase dbt = dbHelper.getReadableDatabase();
        Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
        curse.moveToPosition(curse.getCount() - 1);
        //String entry = cursorCourses.getString()
        String topic = curse.getString(1);
        for (int i = curse.getCount() - 1; i >= 0; i --) {
            curse.moveToPosition(i);
            topic = curse.getString(1);
            if (topic.startsWith("Done: ")) {
                dbHelper.delete(topic);
            }
        }
        Cursor c = dbHelper.readAll();
        mySimAdapter.swapCursor(dbHelper.readAll());
        */
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

    public void addElem(View v) {
        Intent nextIntent = new Intent(this, Add.class);
        //finish();
        addLauncher.launch(nextIntent);
        /*
        String input = elem.getText().toString();
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
        Intent nextIntent = new Intent(this, MainActivity.class);
        //finish();
        addLauncher.launch(nextIntent);
    }

    private void alertView(String message ,final int position ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Inventory.this);
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
                        //SQLiteDatabase dbt = dbHelper.getReadableDatabase();
                        //Cursor curse = dbt.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME, null);
                        //System.out.println(i);
                        //curse.moveToPosition(position);
                        //dbHelper.delete(curse.getString(1));
                        //myAdapter.remove(myAdapter.getItem(position));
                        //Cursor c = dbHelper.readAll();
                        //mySimAdapter.swapCursor(dbHelper.readAll());
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