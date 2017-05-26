package me.jessyan.mvparms.photomark.mvp.model.api.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.mvparms.photomark.app.utils.BitmapUtils;
import me.jessyan.mvparms.photomark.app.utils.FileUtils;
import me.jessyan.mvparms.photomark.app.utils.Utility;

/**
 * Created by zhiPeng.S on 2017/3/17.
 */

public class PhotoManager {
    
    private String error_tip = "The image format is incorrect";
    
    private PhotoManager(){
        
    }

    private static volatile PhotoManager instance;

    public static PhotoManager getInstance() {
        if (instance == null) {
            synchronized (PhotoManager.class) {
                if (instance == null) {
                    instance = new PhotoManager();
                }
            }
        }
        return instance;
    }

    public interface Callback {
        void photo(String path);
    }

    private List<String> photoPaths = new ArrayList<>();
    private Uri photoUri;
    /** 使用照相机拍照获取图片 */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /** 使用相册中的图片 */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    /** 获取到的图片路径 */
    private String picPath = "";


    /**
     * 拍照获取图片
     */
    public void takePhoto(Activity activity) {
        Intent intent = tPhoto(activity);
        if(intent != null)
            activity.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
    }

    public void takePhoto(Fragment activity) {
        Intent intent = tPhoto(activity.getContext());
        if(intent != null)
            activity.startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
    }

    private Intent tPhoto(Context activity){
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            return intent;
        } else {
            Toast.makeText(activity, "内存卡不存在", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /***
     * 从相册中取图片
     */
    public void pickPhoto(Activity activity) {
        activity.startActivityForResult(pPhoto(), SELECT_PIC_BY_PICK_PHOTO);
    }

    public void pickPhoto(Fragment activity) {
        activity.startActivityForResult(pPhoto(), SELECT_PIC_BY_PICK_PHOTO);
    }

    private Intent pPhoto(){
        Intent intent = new Intent();
        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    public void doPhoto(int requestCode, Intent data, Activity activity, Callback callback) {

        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data == null) {
                Toast.makeText(activity, error_tip, Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(activity, error_tip, Toast.LENGTH_LONG).show();
                return;
            }
        }

        String[] pojo = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity.getContentResolver().query(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);

            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }

        // 如果图片符合要求将其上传到服务器
        if (picPath != null && (picPath.endsWith(".png") ||
                picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") ||
                picPath.endsWith(".jpeg") ||
                picPath.endsWith(".JPG"))) {

            BitmapFactory.Options option = new BitmapFactory.Options();
            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
            option.inSampleSize = 3;
            // 根据图片的SDCard路径读出Bitmap
            //Bitmap bm = BitmapFactory.decodeFile(picPath, option);
            Bitmap bm = BitmapUtils.getSmallBitmap(picPath);

            String dateStr = Utility.FORMAT_NUM.format(System.currentTimeMillis());
            int random =(int)(Math.random()*900)+100;
            FileUtils.ratio = 50;
            String filePath = FileUtils.saveFile(activity, dateStr + random +".jpg", bm);

            callback.photo(filePath);
//            photoPaths.add(filePath);
//            uploadPhoto(photoPaths);
        } else {
            Toast.makeText(activity, error_tip, Toast.LENGTH_LONG).show();
        }
    }

    public void uploadAvatar(String path, boolean isUser){
        String url = isUser ? "Account/UserModify.ashx" : "Shop/UpdateShop.ashx";
        String type = isUser ? "HeadImg" : "Logo";
        List<String> paths = new ArrayList<>();
        paths.add(path);
//        uploadPhoto(paths,url,type);
    }

//    public void uploadPhoto(List<String> datas, String url, String type){
//        RequestParams params = new RequestParams(Constant.getBaseUrl() + url);
//        params.addBodyParameter("userid",LoginManager.user(x.app()).userid.get());
//        params.addBodyParameter("modify",type);
//        params.addBodyParameter("content","");
//        params.setMultipart(true);
//        for (int i = 0; i < datas.size(); i++) {
//            if(!datas.get(i).equals("")){
//                params.addBodyParameter("file"+i,new File(datas.get(i)),null); // 如果文件没有扩展名, 最好设置contentType参数.
//            }
//        }
//        LoginManager.addToken(params);
//        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                LoginManager.saveCookie();
//                DialogHelper.toast(x.app().getString(R.string.modify_success));
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//    }

    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    public static final String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory()+ "/" + IMAGE_FILE_NAME; //头像文件路径
    private String urlpath = "";			// 图片本地路径
    private String resultStr = "";	// 服务端返回结果集
    private static ProgressDialog pd;// 等待进度圈
    public static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    public static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    public static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记

    private Intent take(){
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMAGE_FILE_PATH)));
        return takeIntent;
    }

    public void takeAvatarPhoto(Activity activity){
        activity.startActivityForResult(take(), REQUESTCODE_TAKE);
    }

    public void takeAvatarPhoto(Fragment activity){
        activity.startActivityForResult(take(), REQUESTCODE_TAKE);
    }

    private Intent pick(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return pickIntent;
    }

    public void pickAvatarPhoto(Activity activity){
        activity.startActivityForResult(pick(), REQUESTCODE_PICK);
    }

    public void pickAvatarPhoto(Fragment activity){
        activity.startActivityForResult(pick(), REQUESTCODE_PICK);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri, Activity activity) throws NullPointerException {
        activity.startActivityForResult(zoom(uri), REQUESTCODE_CUTTING);
    }

    public void startPhotoZoom(Uri uri, Fragment activity) throws NullPointerException {
        activity.startActivityForResult(zoom(uri), REQUESTCODE_CUTTING);
    }

    private Intent zoom(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    public void setPicToView(Intent picdata, Context activity, Callback callback) {
        if(picdata == null ) return;
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
//            photo = BitmapUtils.getHeaderCircleImage(activity,photo);
            photo = BitmapUtils.compressBitmap(photo,30);
            String dateStr = Utility.FORMAT_NUM.format(System.currentTimeMillis());
            int random =(int)(Math.random()*900)+100;
            FileUtils.ratio = 50;
            urlpath = FileUtils.saveFile(activity, "avatar" + dateStr + random + ".jpg", photo);

            callback.photo(urlpath);

        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data,Activity context,Callback callback) {
        switch (requestCode){
            case PhotoManager.SELECT_PIC_BY_TACK_PHOTO:
            case PhotoManager.SELECT_PIC_BY_PICK_PHOTO:
                PhotoManager.getInstance().doPhoto(requestCode,data,context,callback);
                break;
        }
    }

    public static void onActivityResultForAvatar(int requestCode, int resultCode, Intent data,Activity activity,Callback callback) {
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    PhotoManager.getInstance().startPhotoZoom(data.getData(),activity);
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                PhotoManager.getInstance().startPhotoZoom(Uri.fromFile(new File(PhotoManager.IMAGE_FILE_PATH)),activity);
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                PhotoManager.getInstance().setPicToView(data,activity,callback);
                break;

        }
    }
}
