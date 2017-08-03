package com.teducn.cn.bmobtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.entity.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = null;
    private TextView mTvReg;

    private Button mBtnLogin;

    private EditText mEtUsername;
    private EditText mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initViews();

        //得到当前登录的用户 如果得到则跳转到首页
        BmobUser currentUser = BmobUser.getCurrentUser();
        if (currentUser != null) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            LoginActivity.this.finish();
        }
    }

    private void initViews() {

        mTvReg = (TextView) findViewById(R.id.tv_reg);
        mTvReg.setOnClickListener(this);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reg:
                intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.btn_login:

                if (TextUtils.isEmpty(mEtUsername.getText().toString()) || TextUtils.isEmpty(mEtPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User();
                user.setUsername(mEtUsername.getText().toString());
                user.setPassword(mEtPassword.getText().toString());
                user.login(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            LoginActivity.this.finish();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(User.class)获取自定义用户信息
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }
}
