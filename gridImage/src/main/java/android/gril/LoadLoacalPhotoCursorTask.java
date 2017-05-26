package android.gril;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * User: tc
 * Date: 13-7-9
 * Time: 上午9:59
 */
public class LoadLoacalPhotoCursorTask extends AsyncTask<Object, Object, Object> {
    private Context mContext;
    private final ContentResolver mContentResolver;

    private boolean mExitTasksEarly = false;

    private OnLoadPhotoCursor onLoadPhotoCursor;//定义回调接口，获取解析到的数据

    private ArrayList<Uri> uriArray = new ArrayList<Uri>();
    private ArrayList<Long> origIdArray = new ArrayList<Long>();

    public LoadLoacalPhotoCursorTask(Context mContext) {
        this.mContext = mContext;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    protected Object doInBackground(Object... params) {
        String[] projection = {
                MediaStore.Images.Media._ID
        };
//        Uri uriString = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Uri ext_uri = MediaStore.Images.Media.getContentUri("external");
        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String where = MediaStore.Images.Media.SIZE + ">=?";

        Cursor c = MediaStore.Images.Media.query(
                mContentResolver,
                ext_uri,
                projection,
                where,
                new String[]{1 * 100 * 1024 + ""},
                MediaStore.Images.Media.DATE_ADDED+" desc");
        int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

        int i = 0;
        while (c.moveToNext() && i < c.getCount() && !mExitTasksEarly) {   //移到指定的位置，遍历数据库
            long origId = c.getLong(columnIndex);
            uriArray.add(
                    Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            origId + "")
            );
            origIdArray.add(origId);
            c.moveToPosition(i);
            i++;
        }
        c.close();//关闭数据库
        if (mExitTasksEarly) {
            uriArray = new ArrayList<Uri>();
            origIdArray = new ArrayList<Long>();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (onLoadPhotoCursor != null && !mExitTasksEarly) {
            onLoadPhotoCursor.onLoadPhotoSursorResult(uriArray, origIdArray);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
        mExitTasksEarly = true;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    public void setOnLoadPhotoCursor(OnLoadPhotoCursor onLoadPhotoCursor) {
        this.onLoadPhotoCursor = onLoadPhotoCursor;
    }

    public interface OnLoadPhotoCursor {
        public void onLoadPhotoSursorResult(ArrayList<Uri> uriArray, ArrayList<Long> origIdArray);
    }
}