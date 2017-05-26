package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by zhiPeng.S on 2017/5/15.
 */

public class PType implements Serializable {

    /**
     * typeid : 1
     * name : hot sell
     */

    private int tid;
    private String name;
    private boolean selected;

    public int getTypeid() {
        return tid;
    }

    public void setTypeid(int typeid) {
        this.tid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
