package com.example.notesprop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.notesprop.Helps.Utility;
import com.example.notesprop.Models.Note;
import com.example.notesprop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText etTitle, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        onInit();
        setToolbar();

    }

    private void saveNote() {
        String noteTitle = etTitle.getText().toString();
        String noteContent = etContent.getText().toString();
        if (noteTitle.isEmpty()){
            etTitle.setText(R.string.required_message);
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    private void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    private void onInit() {
        etTitle = findViewById(R.id.etNoteTitle);
        etContent = findViewById(R.id.etNoteContent);

    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.new_note_message);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


    }


}