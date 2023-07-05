package com.example.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.note.bean.Note;
import com.example.note.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private Note note;
    private EditText etTitle, etAuthor,etContent;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private ImageView imageView;
    private String imgP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etContent = findViewById(R.id.et_content);
        imageView = findViewById(R.id.et_picture);

        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            etTitle.setText(note.getTitle());
            etAuthor.setText(note.getAuthor());
            etContent.setText(note.getContent());
            imgP= note.getPicture();
            Bitmap bitmap = BitmapFactory.decodeFile(imgP);
            imageView.setImageBitmap(bitmap);

        }
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    public void save(View view) {
        String title = etTitle.getText().toString();
        String author = etAuthor.getText().toString();
        String content = etContent.getText().toString();
        String picture = imgP;
        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空！");
            return;
        }
        if(TextUtils.isEmpty(author)){
            author="User";
            ToastUtil.toastShort(this,"默认作者为User！");
        }

        note.setTitle(title);
        note.setAuthor(author);
        note.setContent(content);
        note.setPicture(picture);
        note.setCreatedTime(getCurrentTimeForamt());
        long rowId = mNoteDbOpenHelper.updateData(note);
        if (rowId != -1) {
            ToastUtil.toastShort(this, "修改成功");
            this.finish();
        } else {
            ToastUtil.toastShort(this, "修改失败");
        }
    }

    public void insertPicture(View view){
        Intent intent = new Intent(this, Picture.class);
        startActivityForResult(intent,1);

    }
    private String getCurrentTimeForamt() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);

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