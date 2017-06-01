package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhiPeng.S on 2017/5/10.
 */

public class PAtt implements Serializable{
    /**
     * isimage : true
     * ish : false
     * left : 30.0
     * top : 140.0
     * width : 418.0
     * height : 746.0
     * position :  [ {26,134}, {428,134}, {429,855}, {26,855} ]
     * fontsize : 24
     * textcolor : #505050
     * textstring : we are young. just young.
     * layoutgravity : -1
     * gravity : -1
     * topmargin : 16
     * bottommargin : 0
     * leftmargin : 30
     * rightmargin : 0
     * toppadding : 0
     * bottompadding : 0
     * leftpadding : 0
     * rightpadding : 0
     */

    private boolean isimage;
    private boolean ish;
    private double left;
    private double top;
    private double width;
    private double height;
    private int awidth;
    private int aheight;
    private List<String> position;
    private int fontsize;
    private String textcolor;
    private String textstring;
    private int layoutgravity;
    private int gravity;
    private int topmargin;
    private int bottommargin;
    private int leftmargin;
    private int rightmargin;
    private int toppadding;
    private int bottompadding;
    private int leftpadding;
    private int rightpadding;
    /**
     * width : 397
     * height : 92
     * fontid : 8
     * fontname : bigcaslon
     * fontlink : http://192.168.126.63:8888//images/posters/fonts/bigcaslon.ttf
     */

    private int fontid;
    private String fontname;
    private String fontlink;


    public boolean isIsimage() {
        return isimage;
    }

    public void setIsimage(boolean isimage) {
        this.isimage = isimage;
    }

    public boolean isIsh() {
        return ish;
    }

    public void setIsh(boolean ish) {
        this.ish = ish;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAwidth() {
        return awidth;
    }

    public void setAwidth(int awidth) {
        this.awidth = awidth;
    }

    public int getAheight() {
        return aheight;
    }

    public void setAheight(int aheight) {
        this.aheight = aheight;
    }

    public List<String> getPosition() {
        return position;
    }

    public void setPosition(List<String> position) {
        this.position = position;
    }

    public int getFontsize() {
        return fontsize;
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
    }

    public String getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(String textcolor) {
        this.textcolor = textcolor;
    }

    public String getTextstring() {
        return textstring;
    }

    public void setTextstring(String textstring) {
        this.textstring = textstring;
    }

    public int getLayoutgravity() {
        return layoutgravity;
    }

    public void setLayoutgravity(int layoutgravity) {
        this.layoutgravity = layoutgravity;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getTopmargin() {
        return topmargin;
    }

    public void setTopmargin(int topmargin) {
        this.topmargin = topmargin;
    }

    public int getBottommargin() {
        return bottommargin;
    }

    public void setBottommargin(int bottommargin) {
        this.bottommargin = bottommargin;
    }

    public int getLeftmargin() {
        return leftmargin;
    }

    public void setLeftmargin(int leftmargin) {
        this.leftmargin = leftmargin;
    }

    public int getRightmargin() {
        return rightmargin;
    }

    public void setRightmargin(int rightmargin) {
        this.rightmargin = rightmargin;
    }

    public int getToppadding() {
        return toppadding;
    }

    public void setToppadding(int toppadding) {
        this.toppadding = toppadding;
    }

    public int getBottompadding() {
        return bottompadding;
    }

    public void setBottompadding(int bottompadding) {
        this.bottompadding = bottompadding;
    }

    public int getLeftpadding() {
        return leftpadding;
    }

    public void setLeftpadding(int leftpadding) {
        this.leftpadding = leftpadding;
    }

    public int getRightpadding() {
        return rightpadding;
    }

    public void setRightpadding(int rightpadding) {
        this.rightpadding = rightpadding;
    }

    public int getFontid() {
        return fontid;
    }

    public void setFontid(int fontid) {
        this.fontid = fontid;
    }

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public String getFontlink() {
        return fontlink;
    }

    public void setFontlink(String fontlink) {
        this.fontlink = fontlink;
    }
}
