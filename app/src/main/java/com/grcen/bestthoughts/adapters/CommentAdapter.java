package com.grcen.bestthoughts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.grcen.bestthoughts.Bean.Comment;
import com.grcen.bestthoughts.Bean.ExampleBean;
import com.grcen.bestthoughts.Bean.Head;
import com.grcen.bestthoughts.R;
import com.grcen.bestthoughts.activity_image;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ExampleBean> mlist;//adapter的数据源
    public static final int HEAD_TYPE = 00001;
    public static final int BODY_TYPE = 00002;
    public static final int FOOT_TYPE = 00003;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public CommentAdapter(List<ExampleBean> mlist) {
        this.mlist = mlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null)
            context = parent.getContext();
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case HEAD_TYPE:
                view = mLayoutInflater.inflate(R.layout.detailhead, parent, false);
                return new HeadViewHolder(view);
            case BODY_TYPE:
                view = mLayoutInflater.inflate(R.layout.content_item, parent, false);
                return new BodyViewHolder(view);
            case FOOT_TYPE:
                view = mLayoutInflater.inflate(R.layout.detailfoot, parent, false);
                return new FootViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            final Head head = (Head) mlist.get(position);
            ((HeadViewHolder) holder).zantext.setText(head.getZannum() + "");
            ((HeadViewHolder) holder).downtext.setText(head.getZannonum() + "");
            ((HeadViewHolder) holder).contenttext.setText(head.getContent() + "");
            ((HeadViewHolder) holder).sharetext.setText(head.getSharenum() + "");
            loadIntoUseFitWidth(context, head.getImageId(), R.mipmap.load, ((HeadViewHolder) holder).imageView);

            ((HeadViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_image.class);
                intent.putExtra(activity_image.IMAGE_URL,head.getImageId());
                context.startActivity(intent);
            }
        });
            ((HeadViewHolder) holder).up.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                int zannew = head.getZannum()+1;
                ((HeadViewHolder) holder).zantext.setText(zannew+ " ");
                ((HeadViewHolder) holder).zantext.setTextColor(R.color.zancolor);
                ((HeadViewHolder) holder).zanimage.setImageResource(R.mipmap.up);
            }
        });
            ((HeadViewHolder) holder).down.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                int zannonew = head.getZannonum()+1;
                ((HeadViewHolder) holder).downtext.setText(zannonew+ " ");
                ((HeadViewHolder) holder).downtext.setTextColor(R.color.zancolor);
                ((HeadViewHolder) holder).zannoimage.setImageResource(R.mipmap.down);
            }
        });
        } else if (holder instanceof BodyViewHolder) {
//            ((BodyViewHolder) holder).body.setText((CharSequence) listData.get(position-headCount));
            final Comment comment = (Comment) mlist.get(position);
            ((BodyViewHolder) holder).userid.setText(comment.getName());
            ((BodyViewHolder) holder).upnum.setText(comment.getZannum() + "");
            ((BodyViewHolder) holder).downnum.setText(comment.getDownnum() + "");
            ((BodyViewHolder) holder).content.setText(comment.getContent());
            Glide.with(((BodyViewHolder) holder).itemView.getContext()).load(comment.getIconId()).placeholder(R.mipmap.usericon).thumbnail(0.1f).error(R.mipmap.oherro).into(((BodyViewHolder) holder).Iconimage);
            ((BodyViewHolder) holder).zanbutton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    int zannew = comment.getZannum() + 1;
                    ((BodyViewHolder) holder).upnum.setText(zannew + "");
                    ((BodyViewHolder) holder).upnum.setTextColor(R.color.zancolor);
                    ((BodyViewHolder) holder).zanbutton.setImageResource(R.mipmap.up);
                }
            });
            ((BodyViewHolder) holder).zannobutton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    int zannonew = comment.getDownnum() + 1;
                    ((BodyViewHolder) holder).downnum.setText(zannonew + "");
                    ((BodyViewHolder) holder).downnum.setTextColor(R.color.zancolor);
                    ((BodyViewHolder) holder).zannobutton.setImageResource(R.mipmap.down);
                }
            });

        } else if (holder instanceof FootViewHolder) {

        }

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public int getItemViewType(int position) {
        if (mlist.size() > 0) {
            return mlist.get(position).getViewType();
        }
        return super.getItemViewType(position);
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
            zannoimage = (ImageView) itemView.findViewById(R.id.downimage);
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
        private BodyViewHolder(View itemView) {
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
                .error(R.mipmap.oherro)
                .into(imageView);
    }
}
