package com.teducn.cn.bmobtest.entity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by tarena on 2017/8/2.
 */

public class FriendCircleMessage extends BmobObject {

    private User author;

    private String messageText;

    public List<String> getImagePaths() {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    private List<String> imagePaths;

    public FriendCircleMessage(String tableName) {
        super(tableName);
    }

    public FriendCircleMessage(String tableName, User author, String messageText, List<String> messageImages) {
        super(tableName);
        this.author = author;
        this.messageText = messageText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
