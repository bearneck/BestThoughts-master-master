package com.grcen.bestthoughts.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.grcen.bestthoughts.Bean.Picture;
import com.grcen.bestthoughts.R;
import com.grcen.bestthoughts.adapters.PictureAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment-mm";
    public static final String POSITION = "POSITION";
    private Context mContext;
    private int mPosition;


    public static MainFragment newInstance(int position) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        switch (mPosition) {
            // gif界面
            case 0:{
                view = inflater.inflate(R.layout.fragment_gif, container, false);
                initGifView(view);
                break;
            }
            // 视频界面
            case 1:{
                view = inflater.inflate(R.layout.fragment_video, container, false);
                initVideoView(view);
                break;
            }
            // 图片界面
            case 2: {
                view = inflater.inflate(R.layout.fragment_picture, null);
                initPictureView(view);
                break;
            }
            // 文字界面
            case 3:{
                view = inflater.inflate(R.layout.fragment_text, container, false);
                initTextView(view);
                break;
            }
            default:
                Log.i(TAG, "onCreateView: 未知错误");
        }
        return view;
    }

    // 初始化文字界面控件
    private void initTextView(View view) {

    }

    // 初始化图片界面控件
    private Picture[] pictures = {
            new Picture("QQ","好帅",233,666,999,"https://graph.baidu.com/resource/1025ad5cdbcfb098df3b401551352015.jpg","https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=950331994,3932022251&fm=85&s=7A94E6064F445747064E12740300806C",0,"18682920",27312135),
            new Picture("老陈","你也是是真的牛逼",123,126,559,"https://upload.jianshu.io/users/upload_avatars/6560575/d69fb270-103a-4eec-b070-d5e7aa2e9c96.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96","https://cdn2.jianshu.io/assets/web/web-note-ad-1-c2e1746859dbf03abe49248893c9bea4.png",0,"18682920",27312135)

    };
    public static final int UPDATE_ADAPTER = 1;
    private List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private int page = 0;
    private Handler handler  = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_ADAPTER :
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("ResourceAsColor")
    private void initPictureView(View view) {
        initPictures();
        sendREquestWithOkHttp();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(pictureList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.zancolor);//改变刷新颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pictureList.clear();//随机调取数据
                sendREquestWithOkHttp();
                refreshPictures();
            }
        });
    }


    private void sendREquestWithOkHttp() {
        String pagestr = String.valueOf(page);
        final RequestBody requestBody = new FormBody.Builder().
                add("type","3").add("page",pagestr).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.apiopen.top/satinGodApi")
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
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");
                    Log.d(TAG, msg+"你好parseJSONWithJsonObject: "+jsonData);
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.d(TAG, msg+"你好parseJSONWithJsonObject: "+data);
                    for (int i =0;i<data.length();i++){
                        JSONObject jsonObject1 = data.getJSONObject(i);
                         String type = jsonObject1.getString("type"); //文章类型
                         String username = jsonObject1.getString("username");//用户名
                         String header = jsonObject1.getString("header"); //头像
                         String text = jsonObject1.getString("text");//内容
                         String uid = jsonObject1.getString("uid");//用户号
                         int soureid = jsonObject1.getInt("soureid");//文章号
                         int comment = jsonObject1.getInt("comment");//评论数
                         int up = jsonObject1.getInt("up");//赞
                         int down = jsonObject1.getInt("down");//踩
                         int forward = jsonObject1.getInt("forward");//分享
                         String image = jsonObject1.getString("image");//图片链接
                         String thumbnail = jsonObject1.getString("thumbnail");//略缩图链接
                         Log.d(TAG, msg+"你好parseJSONWithJsonObject: "+username);
                         Picture pictures1 = new Picture(username,text,up,comment,forward,header,thumbnail,down,uid,soureid);
                         pictureList.add(pictures1);
                    }
                    Message message = new Message();
                    message.what = UPDATE_ADAPTER;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void refreshPictures() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //CONTEXT:getActivity().getApplicationContext()
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        initPictures();
//                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initPictures() {
        if (pictureList == null) {
            pictureList.clear();//随机调取数据
            for (int i =0;i<3;i++) {
                Random random = new Random();
                int index = random.nextInt(pictures.length);
                pictureList.add(pictures[index]);
            }
        }else {

        }
    }


    // 初始化视频界面控件
    private void initVideoView(View view) {

    }

    // 初始化Gif界面控件
    private void initGifView(View view) {

    }

}
