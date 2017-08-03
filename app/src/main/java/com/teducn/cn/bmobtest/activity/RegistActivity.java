package com.teducn.cn.bmobtest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegistActivity extends Activity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mErVerifyPassword;
    private Button mBtnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initViews();
    }

    private void initViews() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnReg = (Button) findViewById(R.id.btn_reg);
        mBtnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reg:
                if (TextUtils.isEmpty(mEtUsername.getText().toString()) || TextUtils.isEmpty(mEtPassword.getText().toString())) {
                    Toast.makeText(RegistActivity.this, "用户名密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User();
                user.setUsername(mEtUsername.getText().toString());
                user.setPassword(mEtPassword.getText().toString());
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User myUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegistActivity.this, "注册完成", Toast.LENGTH_SHORT).show();
                            RegistActivity.this.finish();
                        } else {
                            Toast.makeText(RegistActivity.this, "注册失败" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}
