package android.gril.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.gril.R;
import android.gril.bean.PhotoUpImageItem;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SelectedImagesAdapter extends BaseAdapter {

	private ArrayList<PhotoUpImageItem> arrayList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public SelectedImagesAdapter(Context context,ArrayList<PhotoUpImageItem> arrayList){
		this.arrayList = arrayList;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.album_default_loading_pic) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.album_default_loading_pic) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.album_default_loading_pic) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build(); // 创建配置过的DisplayImageOption对象
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.selected_images_adapter_item, parent, false);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.selected_image_item);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		imageLoader.displayImage("file://"+arrayList.get(position).getImagePath(), holder.imageView,
				options);
		return convertView;
	}
	class Holder{
		ImageView imageView;
	}
}
