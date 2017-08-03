package com.teducn.cn.bmobtest.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.adapter.FriendCircleAdapter;
import com.teducn.cn.bmobtest.entity.FriendCircleMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CircleFragment extends Fragment {

    private ListView mLvFriendCircle;
    private FriendCircleAdapter adapter;
    private List<FriendCircleMessage> friendCircleMessageList;

    private SwipeRefreshLayout mSwipeRefresh;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_circle, null);
        friendCircleMessageList = new ArrayList<>();
        handler = new Handler();
        initViews(home);
        initData();
        return home;
    }

    private void initViews(View home) {
        mLvFriendCircle = (ListView) home.findViewById(R.id.lv_friendcircle);
        mSwipeRefresh = (SwipeRefreshLayout) home.findViewById(R.id.swipelayout);
        mSwipeRefresh.setColorSchemeColors(Color.parseColor("#ec407a"), Color.parseColor("#03a9f4"), Color.parseColor("#4CAF50"), Color.parseColor("#FFC107"), Color.parseColor("#FF5722"));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        mSwipeRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void initData() {
        BmobQuery<FriendCircleMessage> query = new BmobQuery<>();
        // 排序，按照时间倒序
        query.order("-createdAt");
        // 包含某个对象类型字段的详细信息
        query.include("author");
        query.setLimit(20);  // 设置请求数量20条
        query.findObjects(new FindListener<FriendCircleMessage>() {
            @Override
            public void done(List<FriendCircleMessage> list, BmobException e) {
                adapter = new FriendCircleAdapter(getActivity(), list);
                mLvFriendCircle.setAdapter(adapter);
            }
        });
    }
}
