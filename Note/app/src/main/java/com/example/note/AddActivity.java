package com.example.note;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.note.bean.Note;
import com.example.note.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle,etAuthor,etContent;
    private ImageView imageView;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private String imgP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etContent = findViewById(R.id.et_content);
        imageView = findViewById(R.id.et_picture);
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    public void add(View view) {
            String title = etTitle.getText().toString();
            String author = etAuthor.getText().toString();
            String picture = imgP;
            String content = etContent.getText().toString();
            if(TextUtils.isEmpty(title)){
                ToastUtil.toastShort(this,"标题不能为空！");
                return;
            }
            if(TextUtils.isEmpty(author)){
                author="User";
                ToastUtil.toastShort(this,"默认作者为User！");
            }

            Note note = new Note();
            note.setTitle(title);
            note.setAuthor(author);
            note.setContent(content);
            note.setPicture(picture);

            note.setCreatedTime(getCurrentTimeForamt());
            long row = mNoteDbOpenHelper.insertData(note);
            if(row !=-1){
                ToastUtil.toastShort(this,"添加成功");
                this.finish();
            }else{
                ToastUtil.toastShort(this,"添加失败");
            }
    }
    public void insertPicture(View view){
        Intent intent = new Intent(this, Picture.class);
        startActivityForResult(intent,1);

    }

    private String getCurrentTimeForamt() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode ==RESULT_OK){
                    imgP = data.getStringExtra("imgP");
                    Bitmap bitmap = BitmapFactory.decodeFile(imgP);
                    imageView.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }
}



