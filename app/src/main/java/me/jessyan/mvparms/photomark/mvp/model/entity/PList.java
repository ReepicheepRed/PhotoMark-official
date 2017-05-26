package me.jessyan.mvparms.photomark.mvp.model.entity;


import java.io.Serializable;

/**
 * Created by zhiPeng.S on 2017/5/9.
 */

public class PList implements Serializable{
    /**
     * pid : 1
     * pno : p-123456
     * pname : postertest
     * type : 1
     * originalsrc : left_img.png
     * thumbnailsrc : 1
     * width : 674
     * height : 1198
     */

    private int pid;
    private String pno;
    private String pname;
    private int type;
    private String originalsrc;
    private String thumbnailsrc;
    private String backgroundsrc;
    private String width;
    private String height;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOriginalsrc() {
        return originalsrc;
    }

    public void setOriginalsrc(String originalsrc) {
        this.originalsrc = originalsrc;
    }

    public String getThumbnailsrc() {
        return thumbnailsrc;
    }

    public void setThumbnailsrc(String thumbnailsrc) {
        this.thumbnailsrc = thumbnailsrc;
    }

    public String getBackgroundsrc() {
        return backgroundsrc;
    }

    public void setBackgroundsrc(String backgroundsrc) {
        this.backgroundsrc = backgroundsrc;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
