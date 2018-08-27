package com.example.android2.noteswithsql;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android2.noteswithsql.adapter.getNotesAdapter;
import com.example.android2.noteswithsql.helper.GridSpacingItemDecoration;
import com.example.android2.noteswithsql.helper.RecyclerTouchListener;
import com.example.android2.noteswithsql.helper.databaseHelper;
import com.example.android2.noteswithsql.model.notesData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements getNotesAdapter.EvidenceAdapterListener{
    FloatingActionButton fab;
    databaseHelper myDataBase;
    Button getAllData;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView noDataFound;
    private Dialog mDialog;
    private ArrayList<notesData> notesList;
    getNotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Notes");
        myDataBase = new databaseHelper(this);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noDataFound = findViewById(R.id.noDataFound);
        getAllData =findViewById(R.id.getAllData);
        getAllData.setVisibility(View.GONE); notesList = new ArrayList<>();
        notesList.clear();
        adapter = new getNotesAdapter(this, notesList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilize();
    }

    private void initilize() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, createUpdateNotes.class);
                i.putExtra("noteId", "");
                i.putExtra("noteText", "");
                i.putExtra("activityType", "create");
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        getAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = myDataBase.getAllData();
                if (cursor.getCount() == 0){
                    showMessage("ERROR :-(","Nothing found");
                    return;
                }

                StringBuffer stringBuffer = new StringBuffer();
                while (cursor.moveToNext()){
                    stringBuffer.append("ID: "+cursor.getString(0)+"\n");
                    stringBuffer.append("Text: "+cursor.getString(1)+"\n");
                }
                showMessage("DATA",stringBuffer.toString());


            }
        });

            prepareList();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                notesData notesData = notesList.get(position);
                Intent i = new Intent(MainActivity.this, createUpdateNotes.class);
                i.putExtra("noteId", notesData.getNote_id());
                i.putExtra("noteText", notesData.getNote_text());
                i.putExtra("activityType", "update");
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {
                final notesData notesData = notesList.get(position);
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Note?")
                        .setMessage("Do you want to delete this note permanently?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Integer isDeleted = myDataBase.deleteData(notesData.note_id);
                                if (isDeleted > 0){
                                    notesList.remove(position);

                                    adapter.notifyDataSetChanged();
                                }
                                if (notesList.size()==0){
                                    noDataFound.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_delete);

                // Dismiss any old dialog.
                if (mDialog != null) {
                    mDialog.dismiss();
                }

                // Show the new dialog.
                mDialog = builder.show();

            }
        }));

    }

    private void prepareList() {
        notesList.clear();
        Cursor cursor = myDataBase.getAllData();
        if (cursor.getCount() == 0){
            noDataFound.setVisibility(View.VISIBLE);
            return;
        }

        noDataFound.setVisibility(View.GONE);

        while (cursor.moveToNext()){
            notesList.add(new notesData(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onEvidenceSelected(notesData getStudentData) {


    }

    private void showMessage(String title, String data) {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
