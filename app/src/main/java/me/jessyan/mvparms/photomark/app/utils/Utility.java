package me.jessyan.mvparms.photomark.app.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Locale;

final public class Utility {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static final SimpleDateFormat FORMAT_NUM = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

    public static final int CHINA = 1 ,THAILAND = 1 << 1,ENGLISH = 1 << 2;

    public static void setLanguage(Resources resources, int lanAtr) {
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (lanAtr) {
            case THAILAND:
                config.locale = new Locale("th", "TH");
                break;
            case ENGLISH:
                config.locale = Locale.ENGLISH;
                break;
            case CHINA:
                config.locale = Locale.CHINA;
                break;
            default:
                config.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(config, dm);
    }


}
