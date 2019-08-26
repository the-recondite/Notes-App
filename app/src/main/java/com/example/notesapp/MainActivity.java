package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static ListView notesList;
    static ArrayList<String>notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        ArrayList<String> sets = new ArrayList<>();
        try {
            sets = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", null));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        if (sets == null) {
            notes.add("Example Note");
        }
        else {
            notes = new ArrayList<>(sets);
        }
        notesList = findViewById(R.id.notesList);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        notesList.setAdapter(arrayAdapter);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switchPage(i);
            }
        });
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                try {
                                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
    public void switchPage(int i) {
                Intent intent = new Intent(getApplicationContext(), notesActivity.class);
                intent.putExtra("note", notes.get(i));
                intent.putExtra("Number", i);
                startActivity(intent);
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        notes.add("");
        int num = notes.size()-1;
        arrayAdapter.notifyDataSetChanged();
        switchPage(num);
        return true;
    }
}
