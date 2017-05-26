package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by zhiPeng.S on 2017/5/22.
 */

public class Font implements Serializable {

    /**
     * fid : 1
     * font : arc
     * src : http://192.168.126.63:8888//images/posters/fonts/arc.ttf
     */

    private int fid;
    private String font;
    private String src;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
