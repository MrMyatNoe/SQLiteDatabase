package com.myanmar.tmn.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by msi on 1/2/2018.
 */

public class DictionaryDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "dictionary.db";
    private static final int DB_VERSION = 1;

    private static final String TB_DICTIONARY = "dictionary";

    private static final String FIELD_WORD = "word";
    private static final String DEFINITION = "definition";

    public DictionaryDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_DICTIONARY + "(_id integer PRIMARY KEY," + FIELD_WORD
                + " TEXT, " + DEFINITION + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //save Record
    public void saveRecord(String word, String definition){
        long id = findWordId(word);
        if (id > 0){
            updateRecord(id,word,definition);
        }
        addRecord(word,definition);
    }

    //add Record
    private long addRecord(String word, String definition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_WORD,word);
        values.put(DEFINITION,definition);
        return db.insert(TB_DICTIONARY,null,values);
    }

    //update Record
    public int updateRecord(long id, String word, String defintion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",id);
        values.put(FIELD_WORD,word);
        values.put(DEFINITION,defintion);
        return db.update(TB_DICTIONARY,values,"_id = ?",new String[]{String.valueOf(id)});
    }

    //delete Record
    public int deleteRecord(long id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TB_DICTIONARY,"_id = ?",new String[]{String.valueOf(id)});
    }

    //finding word
    public long findWordId(String word) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT _id FROM " + TB_DICTIONARY + " WHERE " + FIELD_WORD
                       +"= ?" , new String[]{word});
        Log.i("word count","getCount"+c.getColumnCount());
        if (c.getCount() == 1){
            c.moveToFirst();
            returnVal = c.getInt(0);
        }
        c.close();
        return returnVal;
    }

    //find definition
    public String getDefinition(long id){
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT definition FROM " + TB_DICTIONARY + " WHERE _id = ?",
                new String[]{String.valueOf(id)} );
        if (c.getCount() == 1){
            c.moveToFirst();
            returnVal = c.getString(0);
        }
        c.close();
        return returnVal;
    }

    public Cursor getWordList(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id, "+ FIELD_WORD + " FROM " + TB_DICTIONARY + " ORDER BY "
                + FIELD_WORD + " ASC ",null);
    }
}
