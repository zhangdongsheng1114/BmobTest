package com.teducn.cn.bmobtest.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.activity.LoginActivity;
import com.teducn.cn.bmobtest.entity.User;
import com.teducn.cn.bmobtest.util.Utils;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MySettingFragment extends Fragment implements View.OnClickListener {
    private String headURL = "";
    private EditText mEtNickName;
    private Button mBtnSubmit;

    private ImageView mIvHeadIcon;
    private String headIconPath;
    private static final int IMAGE_REQUEST_CODE = 101;
    private User currentUser;
    private String currentHead;
    private BmobFile uploadHead;
    private Button mBtnUnsign;

    private AlertDialog exitDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_my_setting, null);
        initViews(home);
        return home;
    }

    private void initViews(View home) {
        mEtNickName = (EditText) home.findViewById(R.id.et_nickname);
        mBtnUnsign = (Button) home.findViewById(R.id.btn_unsign);
        mBtnSubmit = (Button) home.findViewById(R.id.btn_submit);
        currentUser = BmobUser.getCurrentUser(User.class);
        mIvHeadIcon = (ImageView) home.findViewById(R.id.iv_headicon);
        mIvHeadIcon.setOnClickListener(this);
        mBtnUnsign.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //得到当前登录的用户
        currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            mEtNickName.setText(currentUser.getNick());
        }
        //获取头像信息并显示
        currentHead = currentUser.getHeadPath();
        if (currentHead != null) {
            //图片异步加载的框架
            Picasso.with(getActivity()).load(currentHead).into(mIvHeadIcon);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                final User user = new User();
                user.setNick(mEtNickName.getText().toString());
                //判断是否修改了图片
                if (headIconPath != null) {
                    uploadHead = new BmobFile(new File(headIconPath));
                    uploadHead.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                // toast("上传文件成功:" + bmobFile.getFileUrl());
                                Utils.showToast(getActivity(), "上传头像成功");
                                user.setHeadPath(uploadHead.getFileUrl());
                                user.update(currentUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "更新失败" + e, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Utils.showToast(getActivity(), "上传头像失败" + e);
                            }
                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                            Utils.showToast(getActivity(), "上传进度:" + value + "%");
                        }
                    });
                } else {
                    //没有选择图片 只保存昵称
                    user.update(currentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "更新失败" + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
            case R.id.iv_headicon:
                checkPerm();
                break;
            case R.id.btn_unsign:
                getExitDialog().show();
                break;
        }
    }

    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {   //权限没有被授予
            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {  // 权限被授予
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }
    }

    /**
     * 处理权限中申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "没有申请权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private AlertDialog getExitDialog() {
        if (exitDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage("您确定要退出吗？");
            builder.setCancelable(false);
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    BmobUser.logOut();
                    BmobUser currentUser = BmobUser.getCurrentUser();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("取消", null);
            exitDialog = builder.create();
        }
        return exitDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            // Toast.makeText(getActivity(), "选择头像成功,uri:" + uri, Toast.LENGTH_LONG).show();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            String imagePath = cursor.getString(columnIndex);
            // Toast.makeText(getActivity(), "path:" + imagePath, Toast.LENGTH_LONG).show();
            showImage(imagePath);
            cursor.close();
        }
    }

    private void showImage(final String path) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                if (file.exists()) {
                    headIconPath = path;
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mIvHeadIcon.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getActivity(), "选择头像失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}