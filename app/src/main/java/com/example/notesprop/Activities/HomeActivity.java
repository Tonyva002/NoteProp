package com.example.notesprop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notesprop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onInit();

        addNote.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NoteDetailsActivity.class)));

    }

    private void onInit() {
        addNote = findViewById(R.id.add_note);
    }
}