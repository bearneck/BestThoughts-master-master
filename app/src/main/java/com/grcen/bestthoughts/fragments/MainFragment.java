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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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

    private LinearLayoutManager mLayoutManager;
    private static boolean hasMore = true; // 是否有下一页
    private static int currentPage ;
    // 若是上拉加载更多的网络请求 则不需要删除数据
    private boolean isLoadingMore = false;
    // 最后一个条目位置
    private static int lastVisibleItem = 0;


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
                view = inflater.inflate(R.layout.fragment_picture,  container, false);
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
    RecyclerView mrecyclerView;
    private List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
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
        mrecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mrecyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(pictureList,getContext(),hasMore);

        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mrecyclerView.setLayoutManager(mLayoutManager);
        mrecyclerView.setAdapter(adapter);
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadingMore();
// 初始currentPage为1
        currentPage = 1;

// 网络请求
        sendREquestWithOkHttp(currentPage);

        initPictures();

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.zancolor);//改变刷新颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pictureList.clear();//随机调取数据
                int page = 0;
                sendREquestWithOkHttp(page);
                refreshPictures();
            }
        });

    }

    private void loadingMore(){
        // 实现上拉加载重要步骤，设置滑动监听器，RecyclerView自带的ScrollListener
        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!isLoadingMore){        // 若不是加载更多 才 加载
                    // 在newState为滑到底部时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // 如果没有隐藏footView，那么最后一个条目的位置(带数据）就比我们的getItemCount少1
                        if (!adapter.isFadeTips() && lastVisibleItem + 1 == adapter.getItemCount()) {
                            // 然后调用updateRecyclerview方法更新RecyclerView
                            updateRecyclerView();

                        }
                        // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目(带数据）就要比getItemCount要少2
                        if (adapter.isFadeTips() && lastVisibleItem + 2  == adapter.getItemCount()) {
                            // 然后调用updateRecyclerview方法更新RecyclerView
                            updateRecyclerView();    // 要调
//                            Log.d(TAG, "onScrollStateChanged: 成功运行老子2");
                        }
                    }
                }
            }
            //滚动监听
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//                Log.d(TAG, "onScrolled: 成功运行老子到——》"+dy);
            }

        });
    }
    // 上拉加载时调用的更新RecyclerView的方法
    private void updateRecyclerView() {
        Log.d(TAG, "updateRecyclerView: 成功运行老子1"+hasMore);
        if(hasMore){
            // 还有下一页 网络请求 第二页 第三页
            currentPage++;    // 加1
            isLoadingMore = false;
            sendREquestWithOkHttp(currentPage);
        }}
    private void sendREquestWithOkHttp(int page) {
        String pagestr = String.valueOf(page);//page属性
        final RequestBody requestBody = new FormBody.Builder().
                add("type","3").add("page",pagestr).build();//构建请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.apiopen.top/satinGodApi")//api
                            .post(requestBody)//post
                            .build();//发情post请求
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
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.d(TAG, "parseJSONWithJsonObject: 成功"+data);
                    if (data==null){
                        hasMore=false;
                    }
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
