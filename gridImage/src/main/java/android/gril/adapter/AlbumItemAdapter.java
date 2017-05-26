package android.gril.adapter;

import java.util.List;

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
import android.widget.CheckBox;
import android.widget.ImageView;

public class AlbumItemAdapter extends BaseAdapter {
	private List<PhotoUpImageItem> list;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public AlbumItemAdapter(List<PhotoUpImageItem> list,Context context){
		this.list = list;
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
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.album_item_images_item_view, parent, false);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		//图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
		//这里的uri地址，因为我们现在实现的是获取本地图片，所以使
		//用"file://"+图片的存储地址。如果要获取网络图片，
		//这里的uri就是图片的网络地址。
		imageLoader.displayImage("file://"+list.get(position).getImagePath(), holder.imageView, options);
		holder.checkBox.setChecked(list.get(position).isSelected());
		return convertView;
	}
	
	class Holder{
		ImageView imageView;
		CheckBox checkBox;
	}
}
