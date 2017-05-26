package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by zhiPeng.S on 2017/5/26.
 */

public class Banner implements Serializable{
    /**
     * bid : 3
     * pid : 15
     * type : 0
     * img : http://192.168.126.63:8888//images/banner/poster/15.png
     * link : www.qq.com
     */

    private int bid;
    private int pid;
    private int type;
    private String img;
    private String link;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
