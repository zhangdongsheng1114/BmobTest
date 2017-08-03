package com.teducn.cn.bmobtest.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.entity.FriendCircleMessage;
import com.teducn.cn.bmobtest.entity.User;
import com.teducn.cn.bmobtest.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class UploadFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "tedu";

    @BindView(R.id.imagesLayout)
    RelativeLayout imagesLayout;
    Unbinder unbinder;
    private TextView mTvSend;
    private EditText mEtContent;
    private FriendCircleMessage fcm;
    private ArrayList<ImageView> imageViews = new ArrayList<>();

    private static final int IMAGE_REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_upload, null);
        initViews(home);
        unbinder = ButterKnife.bind(this, home);
        return home;
    }

    private void initViews(View home) {
        mEtContent = (EditText) home.findViewById(R.id.et_content);
        mTvSend = (TextView) home.findViewById(R.id.tv_send);
        mTvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                if (TextUtils.isEmpty(mEtContent.getText().toString())) {
                    Utils.showToast(getActivity(), "内容不能为空");
                    return;
                }
                fcm = new FriendCircleMessage("FriendCircleMessage");
                fcm.setAuthor(BmobUser.getCurrentUser(User.class));
                fcm.setMessageText(mEtContent.getText().toString());
                fcm.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Utils.showToast(getActivity(), "发送成功");
                            mEtContent.setText("");
                            // 判断是否有图片
                            if (imageViews.size() > 0) {
                                String[] imagePaths = new String[imageViews.size()];
                                for (int i = 0; i < imageViews.size(); i++) {
                                    ImageView imageView = imageViews.get(i);
                                    imagePaths[i] = (String) imageView.getTag();
                                }
                                // 批量上传图片
                                BmobFile.uploadBatch(imagePaths, new UploadBatchListener() {
                                    @Override
                                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                                        // 上传完成
                                        fcm.setImagePaths(list1);
                                        fcm.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                Utils.showToast(getContext(), "图片发送完成");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onProgress(int i, int i1, int i2, int i3) {

                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }
                                });
                            }
                        } else {
                            Utils.showToast(getContext(), "发送失败");
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.imageView)
    public void onclick() {  // 添加图片的点击事件
        checkPerm();
    }

    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {   // 没有授权
            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else { // 权限被授予
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            // Toast.makeText(getActivity(), "选择头像成功,uri:" + uri, Toast.LENGTH_LONG).show();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(uri, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            // Toast.makeText(getActivity(), "path:" + imagePath, Toast.LENGTH_LONG).show();
            addImageWithPath(imagePath);
            c.close();
        }
    }

    private void addImageWithPath(String imagePath) {
        final ImageView iv = new ImageView(getActivity());
        int size = (imagesLayout.getWidth() - 2 * 8) / 3;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size, size);
        imagesLayout.addView(iv, lp);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(getActivity()).load(new File(imagePath)).into(iv);
        Log.i(TAG, "addImageWithPath: " + size);
        imageViews.add(iv);
        // 记录图片地址
        iv.setTag(imagePath);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViews.remove(iv);
                refreshImagesLayout();
                imagesLayout.removeView(iv);
            }
        });
        refreshImagesLayout();
    }

    private void refreshImagesLayout() {
        int size = (imagesLayout.getWidth() - 2 * 8) / 3;
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView iv = imageViews.get(i);
            iv.setX(i % 3 * (size + 8));
            iv.setY(i / 3 * (size + 8));
        }
    }
}
