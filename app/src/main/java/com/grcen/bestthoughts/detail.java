package com.grcen.bestthoughts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.grcen.bestthoughts.Bean.Comment;
import com.grcen.bestthoughts.Bean.ExampleBean;
import com.grcen.bestthoughts.Bean.Head;
import com.grcen.bestthoughts.adapters.CommentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.System.load;

public class detail extends Activity {

    public static final String IMAGE_URL = "image_Url";
    public static final String ICON_URL = "icon_Url";
    public static final String ZAN_URL = "zan_Url";
    public static final String DOWN_URL = "down_Url";
    public static final String SHARE_URL = "share_Url";
    public static final String CONTEXT_URL = "context_Url";
    public static final String CONTENT_id = "content_id";

    public Head head = new Head("hahaha", "nihao", 2, 3, 4);
    private Comment[] comments = {new Comment("你好", "哈哈哈", "https://graph.baidu.com/resource/1025ad5cdbcfb098df3b401551352015.jpg", "22222", 666, 233, "")};
    private List<ExampleBean> mlist = new ArrayList<>();
    private CommentAdapter adapter;
    RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle b = getIntent().getExtras();

        String iconurl = b.getString(ICON_URL);
        final String content = b.getString(CONTEXT_URL);
        final String image = b.getString(IMAGE_URL);
        final int zanurl = b.getInt(ZAN_URL);
        final int downurl = b.getInt(DOWN_URL);
        int share = b.getInt(SHARE_URL);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CircleImageView iconbutton = (CircleImageView) findViewById(R.id.icon);
        Glide.with(getApplicationContext()).load(iconurl).error(R.mipmap.oherro).into(iconbutton);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        initHead(content,image,zanurl,downurl,share);
        initComment();
        initFoot();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        initAdapter();
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new CommentAdapter(mlist);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private void initFoot() {
        ExampleBean footBean = new ExampleBean();
        footBean.setViewType(CommentAdapter.FOOT_TYPE);
        mlist.add(footBean);
    }

    private void initHead(String content,String imageid,int zannum,int zannonum,int sharenum) {
        mlist.clear();
        if (content != null){
            Head head = new Head(content, imageid, zannum, zannonum, sharenum);
            head.setViewType(CommentAdapter.HEAD_TYPE);
            mlist.add(head);
        }else {
        Head head = new Head("哦豁，没有评论", "", 233, 233, 233);
            head.setViewType(CommentAdapter.HEAD_TYPE);
            mlist.add(head);
        }
    }

    private void initComment() {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int index = random.nextInt(comments.length);
            comments[index].setViewType(CommentAdapter.BODY_TYPE);
            mlist.add(comments[index]);
        }
    }
}
