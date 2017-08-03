package com.teducn.cn.bmobtest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teducn.cn.bmobtest.R;
import com.teducn.cn.bmobtest.app.MyApp;
import com.teducn.cn.bmobtest.entity.FriendCircleMessage;
import com.teducn.cn.bmobtest.entity.User;

import java.util.List;

/**
 * Created by tarena on 2017/8/3.
 */

public class FriendCircleAdapter extends BaseAdapter<FriendCircleMessage> {
    public FriendCircleAdapter(Context context, List<FriendCircleMessage> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FriendCircleMessage fcm = getData().get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_friendcircle, null);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.item_iv_head);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.item_tv_nickname);
            holder.tv_content = (TextView) convertView.findViewById(R.id.item_tv_content);
            holder.layout_images = (RelativeLayout) convertView.findViewById(R.id.imagesLayout);
            holder.tv_time = (TextView) convertView.findViewById(R.id.item_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = fcm.getAuthor();
        // 异步加载用户头像
        Picasso.with(MyApp.CONTEXT).load(user.getHeadPath()).into(holder.iv_head);
        holder.tv_nickname.setText(user.getNick());
        holder.tv_content.setText(fcm.getMessageText());
        holder.tv_time.setText(fcm.getCreatedAt());
        // 显示图片
        loadImages(holder.layout_images, fcm.getImagePaths());
        return convertView;
    }

    private void loadImages(RelativeLayout layout_images, List<String> imagePaths) {
        // 删除之前显示的所有图片
        layout_images.removeAllViews();
        // 获取屏幕宽度
        int scrrenWidth = MyApp.CONTEXT.getResources().getDisplayMetrics().widthPixels;
        int size = (scrrenWidth - 2 * 8) / 3;
        if (imagePaths.size() == -1) {
            ImageView iv = new ImageView(getContext());
            layout_images.addView(iv, new RelativeLayout.LayoutParams(800, 500));
            Picasso.with(getContext()).load(imagePaths.get(0)).into(iv);
        } else {
            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(getContext()).load(imagePaths.get(i)).into(iv);

                iv.setX(i % 3 * (size + 8));
                iv.setY(i / 3 * (size + 8));
                layout_images.addView(iv, new RelativeLayout.LayoutParams(size, size));
            }
        }
        // 动态改变当复用控件中用到动态改变的控件时自动适应高度可能会出问题
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layout_images.getLayoutParams();
        int line = imagePaths.size() % 3 == 0 ? imagePaths.size() / 3 : imagePaths.size() / 3 + 1;
        lp.height = line * (size + 8);
    }

    class ViewHolder {
        ImageView iv_head;
        TextView tv_nickname;
        TextView tv_content;
        TextView tv_time;
        RelativeLayout layout_images;
    }
}
