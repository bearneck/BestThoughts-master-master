package com.grcen.bestthoughts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.grcen.bestthoughts.R;
import com.grcen.bestthoughts.Bean.Picture;
import com.grcen.bestthoughts.activity_image;
import com.grcen.bestthoughts.detail;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private Context mContext;
    private List<Picture> mPictureList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView iconId;
        ImageView imageId;
        TextView name;
        TextView content;
        TextView zannum;
        TextView contentnum;
        TextView sharenum;
        ImageButton deleteButton;

        ImageView zanimage;
        //评论转发分享
        LinearLayout upview;
        LinearLayout contentview;
        LinearLayout shareview;

        LinearLayout detail;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            iconId = (ImageView) view.findViewById(R.id.icon);
            imageId = (ImageView) view.findViewById(R.id.picture_image);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            zannum = (TextView) view.findViewById(R.id.zan);
            contentnum = (TextView) view.findViewById(R.id.comment);
            sharenum = (TextView) view.findViewById(R.id.share);
            deleteButton = (ImageButton) view.findViewById(R.id.dis);

            zanimage = (ImageView)view.findViewById(R.id.zanimage);
            //评论转发分享
            upview = (LinearLayout) view.findViewById(R.id.upview);
            contentview = (LinearLayout) view.findViewById(R.id.contentview);
            shareview = (LinearLayout) view.findViewById(R.id.shareview);

            detail = (LinearLayout) view.findViewById(R.id.detail);
        }
    }
    public PictureAdapter(List<Picture> pictureList){
        mPictureList = pictureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Picture picture = mPictureList.get(position);
        holder.name.setText(picture.getName());
        holder.content.setText(picture.getContent());
        holder.zannum.setText(picture.getZannum() + " ");//int不能直接转换成string
        holder.contentnum.setText(picture.getContentnum() + " ");
        holder.sharenum.setText(picture.getSharenum() + " ");
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,detail.class);
                Bundle bundle = new Bundle();
                bundle.putString(detail.IMAGE_URL, picture.getImageId());
                bundle.putString(detail.ICON_URL, picture.getIconId());
                bundle.putInt(detail.ZAN_URL,picture.getZannum());
                bundle.putInt(detail.SHARE_URL,picture.getSharenum());
                bundle.putString(detail.CONTEXT_URL,picture.getContent());
                bundle.putInt(detail.CONTENT_id,picture.getSoureid());
                intent.putExtras(bundle);
                mContext.startActivity(intent);//启动TwoActivity活动
            }
        });
        Glide.with(mContext).load(picture.getIconId()).error(R.mipmap.oherro).into(holder.iconId);

        //图片优化
        Glide.with(mContext).load(picture.getImageId())
                .error(R.mipmap.oherro)
                .thumbnail( 0.1f )
                .into(holder.imageId);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPictureList.size()==1){
                    Snackbar.make(v,"再删就没有了",Snackbar.LENGTH_SHORT).show();
                }else {
                    //删除自带默认动画
                    removeData(position);
                }
            }
        });
        holder.upview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onClick(View v) {
                int zannew = picture.getZannum()+1;
                holder.zannum.setText(zannew+ " ");
                holder.zannum.setTextColor(R.color.zancolor);
                holder.zanimage.setImageResource(R.mipmap.up);
            }
        });
        holder.imageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,activity_image.class);
                intent.putExtra(activity_image.IMAGE_URL,picture.getImageId());
                mContext.startActivity(intent);
            }
        });
        holder.contentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,detail.class);
                Bundle bundle = new Bundle();
                bundle.putString(detail.IMAGE_URL, picture.getImageId());
                bundle.putString(detail.ICON_URL, picture.getIconId());
                bundle.putInt(detail.ZAN_URL,picture.getZannum());
                bundle.putInt(detail.SHARE_URL,picture.getSharenum());
                bundle.putString(detail.CONTEXT_URL,picture.getContent());
                intent.putExtras(bundle);

                mContext.startActivity(intent);//启动TwoActivity活动
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPictureList.size();
    }
    public void removeData(int position) {
        mPictureList.remove(position);
        notifyItemRemoved(position);
        if(position != mPictureList.size()){ // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, mPictureList.size() - position);
        }
    }
}
