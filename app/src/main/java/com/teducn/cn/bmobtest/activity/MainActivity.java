package com.teducn.cn.bmobtest.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.fragment.CircleFragment;
import com.teducn.cn.bmobtest.fragment.MySettingFragment;
import com.teducn.cn.bmobtest.fragment.UploadFragment;

public class MainActivity extends FragmentActivity {

    private static FragmentManager fragManager;
    private FragmentTransaction fragTrans;

    private CircleFragment mainCircleFragment;
    private UploadFragment mainUploadFragment;
    private MySettingFragment mainMySettingFragment;

    private LinearLayout mLl_bot;

    private RadioGroup mRg;
    private static final String PG1_NAME = "circle";
    private static final String PG2_NAME = "upload";
    private static final String PG3_NAME = "my";

    private RadioButton mRbLeft;
    private RadioButton mRbMid;
    private RadioButton mRbRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragManager = getSupportFragmentManager();
        initFragment();
    }

    private void dealWithFragmentTransaction(Fragment fragment, String fragmentName) {
        fragTrans = fragManager.beginTransaction();
        fragTrans.hide(fragManager.findFragmentByTag(PG1_NAME));
        fragTrans.add(R.id.fragmentRoot, fragment, fragmentName);
        fragTrans.addToBackStack(fragmentName);
        fragTrans.commit();
    }

    private void initViews() {

        mLl_bot = (LinearLayout) findViewById(R.id.botmenu);

        mRbLeft = (RadioButton) mLl_bot.findViewById(R.id.rb_tab_item_1);
        Drawable[] drawables = mRbLeft.getCompoundDrawables();
        drawables[1].setBounds(1, 1, 45, 45);
        mRbLeft.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);

        mRbMid = (RadioButton) mLl_bot.findViewById(R.id.rb_tab_item_2);
        Drawable[] drawables2 = mRbMid.getCompoundDrawables();
        drawables2[1].setBounds(1, 1, 45, 45);
        mRbMid.setCompoundDrawables(drawables2[0], drawables2[1], drawables2[2], drawables2[3]);

        mRbRight = (RadioButton) mLl_bot.findViewById(R.id.rb_tab_item_3);
        Drawable[] drawables3 = mRbRight.getCompoundDrawables();
        drawables3[1].setBounds(1, 1, 45, 45);
        mRbRight.setCompoundDrawables(drawables3[0], drawables3[1], drawables3[2], drawables3[3]);

        mRg = (RadioGroup) mLl_bot.findViewById(R.id.rg_tab_menu);
        mRg.check(R.id.rb_tab_item_1);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_tab_item_1:
                        popAllExceptTheBottomOne();
                        Log.i("RadioGroup", "rb_tab_item_1");
                        dealWithTextColor(R.id.rb_tab_item_1);
                        break;
                    case R.id.rb_tab_item_2:
                        if (fragManager.findFragmentByTag(PG2_NAME) != null && fragManager.findFragmentByTag(PG2_NAME).isVisible()) {
                            return;
                        }
                        popAllExceptTheBottomOne();
                        Log.i("RadioGroup", "rb_tab_item_2");
                        mainUploadFragment = new UploadFragment();
                        dealWithFragmentTransaction(mainUploadFragment, PG2_NAME);
                        dealWithTextColor(R.id.rb_tab_item_2);
                        break;
                    case R.id.rb_tab_item_3:
                        if (fragManager.findFragmentByTag(PG3_NAME) != null && fragManager.findFragmentByTag(PG3_NAME).isVisible()) {
                            return;
                        }
                        popAllExceptTheBottomOne();
                        Log.i("RadioGroup", "rb_tab_item_3");
                        mainMySettingFragment = new MySettingFragment();
                        dealWithFragmentTransaction(mainMySettingFragment, PG3_NAME);
                        dealWithTextColor(R.id.rb_tab_item_3);
                        break;
                }
            }
        });
    }

    private void dealWithTextColor(int id) {
        switch (id) {
            case R.id.rb_tab_item_1:
                mRbLeft.setTextColor(Color.parseColor("#3c9ef2"));
                mRbMid.setTextColor(Color.parseColor("#8e949d"));
                mRbRight.setTextColor(Color.parseColor("#8e949d"));
                break;
            case R.id.rb_tab_item_2:
                mRbLeft.setTextColor(Color.parseColor("#8e949d"));
                mRbMid.setTextColor(Color.parseColor("#3c9ef2"));
                mRbRight.setTextColor(Color.parseColor("#8e949d"));
                break;
            case R.id.rb_tab_item_3:
                mRbLeft.setTextColor(Color.parseColor("#8e949d"));
                mRbMid.setTextColor(Color.parseColor("#8e949d"));
                mRbRight.setTextColor(Color.parseColor("#3c9ef2"));
                break;
        }
    }

    private void initFragment() {

        popAllExceptTheBottomOne();
        mainCircleFragment = new CircleFragment();
        fragTrans = fragManager.beginTransaction();
        fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        fragTrans.add(R.id.fragmentRoot, mainCircleFragment, PG1_NAME);
        fragTrans.addToBackStack(PG1_NAME);
        fragTrans.commit();
    }

    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
    }

    private static void popAllExceptTheBottomOne() {
        for (int i = 0, count = fragManager.getBackStackEntryCount() - 1; i < count; i++) {
            fragManager.popBackStack();
        }
    }
}
