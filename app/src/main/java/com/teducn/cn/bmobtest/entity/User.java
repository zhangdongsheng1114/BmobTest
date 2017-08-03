package com.teducn.cn.bmobtest.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by tarena on 2017/8/2.
 */

public class User extends BmobUser {

    private String nick;
    private String headPath;

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
