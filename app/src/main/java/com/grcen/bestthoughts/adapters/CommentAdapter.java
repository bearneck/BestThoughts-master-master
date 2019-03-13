package com.grcen.bestthoughts.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.grcen.bestthoughts.Bean.Comment;
import com.grcen.bestthoughts.Bean.Head;
import com.grcen.bestthoughts.R;
import com.grcen.bestthoughts.detail;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Comment> mCommentList;
    private Head mHead;
    private static final int HEAD_TYPE = 00001;
    private static final int BODY_TYPE = 00002;
    private static final int FOOT_TYPE = 00003;
    private int headCount = 1;//头部个数，后续可以自己拓展
    private int footCount = 1;//尾部个数，后续可以自己拓展
    private LayoutInflater mLayoutInflater;

    public void AddHeadAdapter(Context context, Head head) {
        mLayoutInflater = LayoutInflater.from(context);
        mHead = head;
    }

    public CommentAdapter(Context context, List<Comment> commentList) {
        mLayoutInflater = LayoutInflater.from(context);
        mCommentList = commentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mContext==null){
//            mContext = parent.getContext();
//        }
//        View view = LayoutInflater.from(mContext).inflate(R.layout.content_item,parent,false);
//        return new ViewHolder(view);
        switch (viewType) {
            case HEAD_TYPE:
                return new HeadViewHolder(mLayoutInflater.inflate(R.layout.detailhead, parent, false));
            case BODY_TYPE:
                return new BodyViewHolder(mLayoutInflater.inflate(R.layout.content_item, parent, false));
            case FOOT_TYPE:
                return new FootViewHolder(mLayoutInflater.inflate(R.layout.detailfoot, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {

        } else if (holder instanceof BodyViewHolder) {
//            ((BodyViewHolder) holder).body.setText((CharSequence) listData.get(position-headCount));
            Context context;
            Comment comment = mCommentList.get(position - 1);
            ((BodyViewHolder) holder).userid.setText(comment.getName());
            ((BodyViewHolder) holder).upnum.setText(comment.getZannum() + "");
            ((BodyViewHolder) holder).downnum.setText(comment.getDownnum() + "");
            ((BodyViewHolder) holder).content.setText(comment.getContent());
            Glide.with(((BodyViewHolder) holder).itemView.getContext()).load(comment.getIconId()).error(R.mipmap.oherro).into(((BodyViewHolder) holder).Iconimage);
            ((BodyViewHolder) holder).zanbutton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    int zannew = 233 + 1;
                    ((BodyViewHolder) holder).upnum.setText(zannew + " ");
                    ((BodyViewHolder) holder).upnum.setTextColor(R.color.zancolor);
                    ((BodyViewHolder) holder).zanbutton.setImageResource(R.mipmap.up);
                }
            });
            ((BodyViewHolder) holder).zannobutton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    int zannonew = 666 + 1;
                    ((BodyViewHolder) holder).downnum.setText(zannonew + " ");
                    ((BodyViewHolder) holder).downnum.setTextColor(R.color.zancolor);
                    ((BodyViewHolder) holder).zannobutton.setImageResource(R.mipmap.down);
                }
            });

        } else if (holder instanceof FootViewHolder) {

        }

    }

    @Override
    public int getItemCount() {
//        return mCommentList.size();
        return headCount + getBodySize() + footCount;
    }

    //判断头尾部以及内容区域
    private int getBodySize() {
        return mCommentList.size();
    }

    private boolean isHead(int position) {
        return headCount != 0 && position < headCount;
    }

    private boolean isFoot(int position) {
        return footCount != 0 && (position >= (getBodySize() + headCount));
    }

    public int getItemViewType(int position) {
        if (isHead(position)) {
            return HEAD_TYPE;
        } else if (isFoot(position)) {
            return FOOT_TYPE;
        } else {
            return BODY_TYPE;
        }
    }

    private static class HeadViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView contenttext;
        TextView zantext;
        TextView downtext;
        TextView sharetext;
        ImageView zanimage;
        ImageView zannoimage;
        LinearLayout up;
        LinearLayout down;

        public HeadViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            contenttext = (TextView) itemView.findViewById(R.id.content);
            zantext = (TextView) itemView.findViewById(R.id.zan);
            downtext = (TextView) itemView.findViewById(R.id.down);
            sharetext = (TextView) itemView.findViewById(R.id.share);
            zanimage = (ImageView) itemView.findViewById(R.id.zanimage);
            ImageView zannoimage = (ImageView) itemView.findViewById(R.id.downimage);
            up = (LinearLayout) itemView.findViewById(R.id.upview);
            down = (LinearLayout) itemView.findViewById(R.id.downview);
        }
    }

    private static class BodyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CircleImageView Iconimage;
        TextView userid;
        TextView upnum;
        TextView downnum;
        TextView content;
        ImageButton zanbutton;
        ImageButton zannobutton;

        //        TextView body;
        public BodyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView;
            Iconimage = (CircleImageView) itemView.findViewById(R.id.icon);
            userid = (TextView) itemView.findViewById(R.id.name);
            upnum = (TextView) itemView.findViewById(R.id.zan);
            downnum = (TextView) itemView.findViewById(R.id.zanno);
            content = (TextView) itemView.findViewById(R.id.content);
            zanbutton = (ImageButton) itemView.findViewById(R.id.up);
            zannobutton = (ImageButton) itemView.findViewById(R.id.down);
//            body= (TextView) itemView.findViewById(R.id.tv_body);


        }
    }

    private static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }
}
