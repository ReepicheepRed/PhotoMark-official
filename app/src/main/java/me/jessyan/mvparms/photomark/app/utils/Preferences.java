/**
 * com.rightoo.li.login
 * LoginUserUtis.java
 * 2015年2月4日 下午3:36:30
 * @author: z```s
 */
package me.jessyan.mvparms.photomark.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.mvparms.photomark.mvp.model.entity.Poster;


/**
 * <p></p>
 * 2015年2月4日 Administrator
 * @author: z```s
 */
public class Preferences {
	public static final String USER_PREFERENCES_NAME = "application_user";
	public static final String PREFERENCES_RECORD = "application_record";
	public static final String PREFERENCES_LOGIN_FIRST = "login_first";

	private static volatile Preferences instance;
	private Preferences() {
	}

	public static Preferences instance(){
		if(instance == null){
			synchronized (Preferences.class){
				if (instance == null) {
					instance = new Preferences();
				}
			}
		}
		return instance;
	}

	/**
	 * <p>获取共享文件SharedPreferences</p>
	 * @param context
	 * @param name 文件名
	 * @return
	 * 2015年2月4日 下午3:40:20
	 * @author: z```s
	 */
	public static SharedPreferences app(Context context, String name) {
		if (context == null || TextUtils.isEmpty(name)) {
			return null;
		}
		return context.getSharedPreferences(name, Context.MODE_APPEND);
	}
	

	/**
	 *<p>Get User Information<p>
	 * @param context
	 * @return
	 * @data 2016-1-17 下午10:12:54
	 * @author zhiPeng.s
	 */
	public static SharedPreferences user(Context context) {
		return app(context, USER_PREFERENCES_NAME);
	}

//	private List<Poster> posters = new ArrayList<>();

	private SharedPreferences record(Context context) {
		return app(context, PREFERENCES_RECORD);
	}

	public List<Poster> readPoster(Context context) {
		return new GsonBuilder().serializeNulls().create().fromJson(
				record(context).getString("Poster",""),new TypeToken<List<Poster>>(){}.getType());
	}

	public void savePoster(Context context,Poster poster) {
		List<Poster> posters = readPoster(context) == null ? new ArrayList<>() : readPoster(context);
		for (int i = 0; i < posters.size(); i++) {
			if(posters.get(i).getIntro().getPid() == poster.getIntro().getPid())
				posters.remove(i);
		}
		posters.add(poster);
		record(context).edit().putString("Poster",
				new GsonBuilder().serializeNulls().create().toJson(posters)).apply();
	}

	public void clearRecord(Context context){
		record(context).edit().clear().apply();
	}

}