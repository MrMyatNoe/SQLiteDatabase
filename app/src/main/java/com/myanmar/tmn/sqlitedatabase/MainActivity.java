package com.myanmar.tmn.sqlitedatabase;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ed_word)
    EditText word;

    @BindView(R.id.ed_definition)
    EditText definition;

    @BindView(R.id.btn_save)
    Button btn;

    DictionaryDatabase db;

    @BindView(R.id.lv)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this,this);

        db = new DictionaryDatabase(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,db.getDefinition(id),Toast.LENGTH_SHORT)
                        .show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("R u sure?");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "deleted "
                                , Toast.LENGTH_LONG).show();
                        updateWordList();
                    }
                });
                builder.create().show();
                db.deleteRecord(id);
                return true;
            }
        });
    }

    private void saveRecord(){
        db.saveRecord(word.getText().toString(),definition.getText().toString());
        word.setText("");
        definition.setText("");
        updateWordList();
    }

    private void updateWordList() {
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,db.getWordList(),new String[]{"word"}
                ,new int[]{android.R.id.text1},0);
        listView.setAdapter(simpleCursorAdapter);
    }
}
