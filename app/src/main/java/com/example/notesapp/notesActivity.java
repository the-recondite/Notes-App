package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class notesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    EditText editText;
    int num;
    MainActivity mainActivity = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        num = intent.getIntExtra("Number", 0);
        editText = findViewById(R.id.editText);
        editText.setText(note);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainActivity.notes.set(num, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                try {
                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        mainActivity.notes.set(num, editText.getText().toString());
        MainActivity.arrayAdapter.notifyDataSetChanged();
        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finish();
        return true;
    }

}
