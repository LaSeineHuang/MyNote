package com.example.note;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.note.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "noteSQLLite.db";
    private static final String TABLE_NAME_NOTE = "note";

    private static final String CREATE_TABLE_SQL = "create table "+ TABLE_NAME_NOTE +"(id integer primary key autoincrement,title text,author text,content text,imgP text,create_time text)";

    private Context mcontext;
    public NoteDbOpenHelper(Context context){super(context,DB_NAME,null,1); mcontext=context;}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        Toast.makeText(mcontext, "数据库创建成功", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertData(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("author",note.getAuthor());
        values.put("content",note.getContent());
        values.put("imgP",note.getPicture());
        values.put("create_time",note.getCreatedTime());

        return db.insert(TABLE_NAME_NOTE,null,values);
    }

    public List<Note> queryAllFromDb(){
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE,null,null,null,null,null,null);
        if(cursor !=null){
            while(cursor.moveToNext()){
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String picture = cursor.getString(cursor.getColumnIndex("imgP"));
                @SuppressLint("Range") String createTime = cursor.getString(cursor.getColumnIndex("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setAuthor(author);
                note.setContent(content);
                note.setPicture(picture);
                note.setCreatedTime(createTime);

                noteList.add(note);

            }
            cursor.close();
        }
        return noteList;
    }

    public List<Note> queryFromByTitle(String title){
        if(TextUtils.isEmpty(title)){
            return queryAllFromDb();
        }
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE,null,"title like ?",new String[]{"%"+title+"%"},null,null,null);

        if(cursor !=null){
            while(cursor.moveToNext()){
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title2 = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String author2 = cursor.getString(cursor.getColumnIndex("author"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String picture = cursor.getString(cursor.getColumnIndex("imgP"));
                @SuppressLint("Range") String createTime = cursor.getString(cursor.getColumnIndex("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title2);
                note.setAuthor(author2);
                note.setContent(content);
                note.setPicture(picture);
                note.setCreatedTime(createTime);

                noteList.add(note);

            }
            cursor.close();
        }
        return noteList;    }

    public int deleteFromDbById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME_NOTE,"id = ?",new String[]{id});
    }
    public int updateData(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("author",note.getAuthor());
        values.put("content",note.getContent());
        values.put("imgP",note.getPicture());
        values.put("create_time",note.getCreatedTime());
        return db.update(TABLE_NAME_NOTE,values,"id like ?",new String[]{note.getId()});
    }

}
