package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiPeng.S on 2017/5/10.
 */

public class Poster implements Serializable{

    private PList intro;

    private List<PAtt> atts;

    public PList getIntro() {
        return intro;
    }

    public void setIntro(PList intro) {
        this.intro = intro;
    }

    public List<PAtt> getAtts() {
        return atts;
    }

    public void setAtts(List<PAtt> atts) {
        this.atts = atts;
    }

}
