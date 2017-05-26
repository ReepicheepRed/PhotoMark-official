package android.gril.bean;

import java.io.Serializable;

/**
 * Created by zhiPeng.S on 2016/12/5.
 */

public class SelectedPic implements Serializable{
    private int id;
    private String content;

    public SelectedPic(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
