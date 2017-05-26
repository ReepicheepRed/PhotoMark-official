package android.gril;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: tc
 * Date: 13-7-9
 * Time: 上午9:57
 */
public class ImageAdapter extends BaseAdapter {
    private ImageWorker imageWorker;

    private HashMap<Long, Boolean> seletedMap = new HashMap<Long, Boolean>();
    private ArrayList<Long> origIdArray = new ArrayList<Long>();

    private LayoutInflater mInflater;

    public ImageAdapter(ImageWorker imageWorker, Context context) {
        this.imageWorker = imageWorker;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return origIdArray.size();
    }

    @Override
    public Object getItem(int position) {
        return origIdArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return origIdArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, parent, false);

            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.imageView);
            holder.select = (CheckBox) convertView.findViewById(R.id.select_btn);
            convertView.setTag(R.id.holder_tag, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder_tag);
        }
        final long origId = origIdArray.get(position);
        holder.select.setChecked(seletedMap.containsKey(origId) ? seletedMap.get(origId) : false);
        //加载图片
        imageWorker.loadImage(origId, holder.img);
        return convertView;
    }

    public ImageAdapter putSelectMap(Long origId, Boolean isChecked) {
        seletedMap.put(origId, isChecked);
        return this;
    }

    public ImageAdapter setOrigIdArray(ArrayList<Long> origIdArray) {
        this.origIdArray = origIdArray;
        return this;
    }

    public static class ViewHolder {
        ImageView img;
        CheckBox select;
    }
}
