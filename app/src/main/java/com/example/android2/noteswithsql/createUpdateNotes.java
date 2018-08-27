package com.example.android2.noteswithsql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android2.noteswithsql.helper.databaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class createUpdateNotes extends AppCompatActivity {

    ImageView btnBack;
    TextView currentDate,btnNext;
    EditText etNotes;
    databaseHelper myDatabase;
    String intentNoteId,intentNoteText,activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        btnBack = findViewById(R.id.backBtn);
        currentDate = findViewById(R.id.currentDate);
        btnNext = findViewById(R.id.btnNext);
        etNotes = findViewById(R.id.etNotes);
        myDatabase = new databaseHelper(this);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM HH:mm a");
        final String formattedDate = df.format(c);
        currentDate.setText(formattedDate);
        btnNext.setVisibility(View.GONE);
        intentNoteId = getIntent().getStringExtra("noteId");
        intentNoteText = getIntent().getStringExtra("noteText");
        activityType = getIntent().getStringExtra("activityType");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etNotes.setText(intentNoteText);
        etNotes.addTextChangedListener(mTextEditorWatcherCode);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etNotes.getText().toString().trim().equals("")){

                    if (activityType.equals("create")){
                        boolean isInserted = myDatabase.insertData(etNotes.getText().toString(),formattedDate);
                        if (isInserted){
                            Toast.makeText(createUpdateNotes.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            createUpdateNotes.this.finish();

                        }else {
                            Toast.makeText(createUpdateNotes.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }else if (activityType.equals("update")){
                        boolean isUpdated = myDatabase.updateData(intentNoteId,etNotes.getText().toString(),formattedDate);
                        if (isUpdated){
                            Toast.makeText(createUpdateNotes.this, "Data Updated", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            createUpdateNotes.this.finish();

                        }else {
                            Toast.makeText(createUpdateNotes.this, "Data not updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(createUpdateNotes.this, "Please insert some note.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private final TextWatcher mTextEditorWatcherCode = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            //inputgroupname.setText(s.toString());
            // textCount.setText(String.valueOf(s.length()));
        }

        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length()==0){
                btnNext.setVisibility(View.GONE);
            }else {
                btnNext.setVisibility(View.VISIBLE);
            }
        }
    };
}
