package com.grcen.bestthoughts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.grcen.bestthoughts.Bean.Picture;
import com.grcen.bestthoughts.adapters.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static java.lang.System.load;

public class detail extends Activity {
    public static final int UPDATE_ADAPTER = 1;
    public static final int FAITL_LOAD = 2;

    public static final String IMAGE_URL = "image_Url";
    public static final String ICON_URL = "icon_Url";
    public static final String ZAN_URL = "zan_Url";
    public static final String DOWN_URL = "down_Url";
    public static final String SHARE_URL = "share_Url";
    public static final String CONTEXT_URL = "context_Url";
    public static final String CONTENT_id = "content_id";

    public Head head = new Head("hahaha", "nihao", 2, 3, 4);
    private Comment[] comments = {new Comment("你好", "哈哈哈", "https://graph.baidu.com/resource/1025ad5cdbcfb098df3b401551352015.jpg", "22222", 666, 233)};
    private List<ExampleBean> mlist = new ArrayList<>();
    private CommentAdapter adapter;
    RecyclerView recyclerView;
    private Context context;
    private int page;
    private int id;

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_ADAPTER :
                    adapter.notifyDataSetChanged();
                    break;
                case FAITL_LOAD:
                    Toast.makeText(getApplicationContext(),"没有评论",Toast.LENGTH_SHORT).show();
                    default:
                        break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        int sourceid = b.getInt(CONTENT_id);
        sendREquestWithOkHttp(sourceid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();


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
        final Button follow = (Button) findViewById(R.id.follow);
        final ImageButton collection = (ImageButton)findViewById(R.id.collection);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collection.setImageResource(R.mipmap.shoucanged);
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow.setText("已关注");
            }
        });


        initHead(content,image,zanurl,downurl,share);
//        initComment();
//        initFoot();
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

    private void sendREquestWithOkHttp(int sourceid) {
        String pagestr = String.valueOf(page);
        String idstr = String.valueOf(sourceid);
        final RequestBody requestBody = new FormBody.Builder().
                add("id",idstr).add("page",pagestr).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.apiopen.top/satinCommentApi")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJsonObject(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            public void parseJSONWithJsonObject(String jsonData) {
                try {
                    //第一层
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");
                    Log.d(TAG, msg+"你好parseJSONWithJsonObject: "+jsonData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    Log.d(TAG, msg+"你好parseJSONWithJsonObject: "+data);
                    //第二层data
                    String data1 = data.toString();
                    JSONObject jsonObjectfirst = new JSONObject(data1);
                    int author_uid = jsonObjectfirst.getInt("author_uid");
                    //第三层hot
                    JSONObject hot = jsonObjectfirst.getJSONObject("hot");
//                    //第四层info
//                    JSONObject info = hot.getJSONObject("info");
//                    int count = info.getInt("count");
//                    //第四层list
//                    JSONArray list = hot.getJSONArray("list");
//                    //第五层解析list
//                    for (int i =0;i<list.length();i++){
//                        JSONObject listObject = list.getJSONObject(i);
//                        String content = listObject.getString("content"); //评论
//                        String ctime = listObject.getString("ctime");//时间
//                        int hate_count = listObject.getInt("hate_count"); //踩
//                        int id = listObject.getInt("id");//内容id
//                        int like_count = listObject.getInt("like_count");//赞
//                        JSONArray precmts = listObject.getJSONArray("precmts");//回复
//                        JSONObject user = listObject.getJSONObject("user");//个人信息
//                        int uid = user.getInt("id");//个人id
//                        String uid1 = uid+"";
//                        boolean is_vip = user.getBoolean("is_vip");//是否是vip
//                        String personal_page = user.getString("personal_page");//个人空间
//                        String profile_image = user.getString("profile_image");//头像
//                        String sex = user.getString("sex");
//                        String total_cmt_like_count = user.getString("total_cmt_like_count");
//                        String username = user.getString("username");
//                        Comment comment = new Comment(username,content,profile_image,uid1,like_count,0);
////                        new Comment("你好", "哈哈哈", "https://graph.baidu.com/resource/1025ad5cdbcfb098df3b401551352015.jpg", "22222", 666, 233, ""
//                        comment.setViewType(CommentAdapter.BODY_TYPE);
//                        mlist.add(comment);
//                        Log.d(TAG, "detailparseJSONWithJsonObject: ");
//                        Log.d(TAG, "detailparseJSONWithJsonObject: "+uid1);
//                    }
                    JXJSON(hot);
                    //第三层hot
                    JSONObject normal = jsonObjectfirst.getJSONObject("normal");
                    JXJSON(normal);
                    initFoot();
                    Message message = new Message();
                    message.what = UPDATE_ADAPTER;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void JXJSON(JSONObject hot) throws JSONException {
                //第四层info
                JSONObject info = hot.getJSONObject("info");
                int count = info.getInt("count");
                if (count==0){
                    Message message = new Message();
                    message.what = FAITL_LOAD;
                    handler.sendMessage(message);
                }
                //第四层list
                JSONArray list = hot.getJSONArray("list");
                //第五层解析list
                for (int i =0;i<list.length();i++){
                    JSONObject listObject = list.getJSONObject(i);
                    String content = listObject.getString("content"); //评论
                    String ctime = listObject.getString("ctime");//时间
                    int hate_count = listObject.getInt("hate_count"); //踩
                    int id = listObject.getInt("id");//内容id
                    int like_count = listObject.getInt("like_count");//赞
                    JSONArray precmts = listObject.getJSONArray("precmts");//回复
                    JSONObject user = listObject.getJSONObject("user");//个人信息
                    int uid = user.getInt("id");//个人id
                    String uid1 = uid+"";
                    boolean is_vip = user.getBoolean("is_vip");//是否是vip
                    String personal_page = user.getString("personal_page");//个人空间
                    String profile_image = user.getString("profile_image");//头像
                    String sex = user.getString("sex");
                    String total_cmt_like_count = user.getString("total_cmt_like_count");
                    String username = user.getString("username");
                    Comment comment = new Comment(username,content,profile_image,uid1,like_count,0);
//                        new Comment("你好", "哈哈哈", "https://graph.baidu.com/resource/1025ad5cdbcfb098df3b401551352015.jpg", "22222", 666, 233, ""
                    comment.setViewType(CommentAdapter.BODY_TYPE);
                    mlist.add(comment);
                    Log.d(TAG, "detailparseJSONWithJsonObject: ");
                    Log.d(TAG, "detailparseJSONWithJsonObject: "+uid1);
                }
            }
        }).start();
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
