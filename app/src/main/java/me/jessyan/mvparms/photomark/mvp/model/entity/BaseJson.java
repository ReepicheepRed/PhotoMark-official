package me.jessyan.mvparms.photomark.mvp.model.entity;

import java.io.Serializable;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseJson<T> implements Serializable{
    private T data;
    private String returncode;
    private String code;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 请求是否成功
     * @return
     */
//    public boolean isSuccess() {
//        if (code.equals(Api.RequestSuccess)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
