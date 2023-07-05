package com.example.note.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.EditActivity;
import com.example.note.NoteDbOpenHelper;
import com.example.note.R;
import com.example.note.bean.Note;
import com.example.note.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    public MyAdapter(Context context,List<Note> mBeanList){
        this.mBeanList = mBeanList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);

    }
    @NonNull
    @Override
    //创建
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    //绑定
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvAuthor.setText(note.getAuthor());
        holder.mTvContent.setText(note.getContent());
        //holder.mTvPicture.setText(note.getPicture());
        holder.mTvtime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到编辑页面
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("note",note);
                mContext.startActivity(intent);


            }
        });
        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //长按弹出弹窗，删除或者编辑
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                View view = mLayoutInflater.inflate(R.layout.list_item_dialog_layout,null);
                TextView tvDelete = view.findViewById(R.id.tv_delete);
                TextView tvEdite = view.findViewById(R.id.tv_edit);
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if(row > 0){
                            removeData(position);
                            ToastUtil.toastShort(mContext,"删除成功");
                        }else{
                            ToastUtil.toastShort(mContext,"删除失败");
                        }
                        dialog.dismiss();

                    }
                });

                tvEdite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,EditActivity.class);
                        intent.putExtra("note",note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
                return false;
            }
        });

    }
    @Override
    //链表数量
    public int getItemCount() {
        return mBeanList.size();
    }

    public void refreshData(List<Note> notes){
        this.mBeanList = notes;
        notifyDataSetChanged();

    }

    public void removeData(int pos){
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        TextView mTvAuthor;
        TextView mTvContent;
        TextView mTvPicture;
        TextView mTvtime;
        ViewGroup rlContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvAuthor =itemView.findViewById(R.id.tv_author);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvtime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.r1_item_container);
        }
    }


}
