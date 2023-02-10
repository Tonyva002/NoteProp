package com.example.notesprop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    private ImageButton btnSabe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        onInit();

        btnSabe.setOnClickListener(v -> saveNote());
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

    private void onInit() {
        etTitle = findViewById(R.id.etNoteTitle);
        etContent = findViewById(R.id.etNoteContent);
        btnSabe = findViewById(R.id.saveNote);
    }


}